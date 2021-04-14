package com.badiplist.source;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BadiplistApplication {
	private static final Logger log = LoggerFactory.getLogger(BadiplistApplication.class);


	public static void main(String[] args) throws IOException, GitAPIException {
		ApplicationContext applicationContext =  SpringApplication.run(BadiplistApplication.class, args);
		GetRepo getRepo = applicationContext.getBean(GetRepo.class);
		getRepo.cloneTheRepo();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}




}

@Service
class GetRepo {
	private static final String _url = "https://api.github.com/repos/firehol/blocklist-ipsets/contents?ref=master&userid=ChrisPendergraftSF";
	public int indexCount = 0;
	public int pageLength = 0;
	private String[] filelist;
	public Boolean ipsetsComplete = false;
	public void setFilelist( String[] filelist){
		this.filelist = filelist;
		pageLength = filelist.length;
	}
	public String[] getFilelist(){
		return this.filelist;
	}


	@Autowired
	ListSourceRepository srcrepo;
	@Autowired
	BadIpsRepository badipsrepo;

	public String readFile(String path, Charset encoding)
			throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	public void parseRepo() throws IOException {
			if(indexCount<pageLength) {
				String pathAsString = readFile("repo/" + filelist[indexCount], StandardCharsets.UTF_8);

				goParseFile(pathAsString);
			}else{

				if(!ipsetsComplete){
					System.out.println("IPSETS INGEST COMPLETE, GETTING NETSETS");
					ipsetsComplete = true;
					indexCount=0;
					setFilelist(getRepoNetSet());
					parseRepo();

				}else{
					System.out.println("IPSETS & NETSET INGEST COMPLETE, REST (HAL) IS READ :8080/badips/");
				}

			}

	}
	public String[] getRepoNetSet(){
		String[] pathnames;
		File f = new File("repo");

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(".netset");
			}
		};
		pathnames = f.list(filter);


		return pathnames;
	}
	public String[] getRepoIpSets(){
		String[] pathnames;
		File f = new File("repo");

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(".ipset");
			}
		};
		pathnames = f.list(filter);


 		return pathnames;
	}

	public void cloneTheRepo()  throws IOException, IllegalStateException, GitAPIException{
		//First delete local repo that was built on last startup
		Path rootPath = Paths.get("repo");
		FileUtils.deleteDirectory(new File("repo"));


		//Now go attempt a clone of the repo

		try (

		Git result = Git.cloneRepository()
					.setURI("https://github.com/firehol/blocklist-ipsets.git")
					.setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
					.setDirectory(new File("repo"))
					.call();
		){
			result.getRepository().close();
			//after download, now get the filelist, set it on the controller, and call parse repo for first time;
			setFilelist(getRepoIpSets());
			parseRepo();


		}
		catch(GitAPIException exp){
			System.out.println(exp);
		}


	}
	public void saveSource(ListSourceContent content) throws IOException {

		//save source
		ListSource src = content.getSource();
		System.out.println("=============content "+ content.getSource().getHostName());
		System.out.println("=============content "+ content.getFinalips().size());
		srcrepo.save(src);
		//save badips
		List<BadIp> badips = content.getFinalips();
		badips.forEach(item->{

			badipsrepo.save(item);

		});

		TimerTask update = new TimerTask() {
			public void run() {

				try {
					parseRepo();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Timer timer = new Timer("Timer2");
		long delay = 200L;
		timer.schedule(update, delay);




	}
	public void goParseFile(String inputs) throws IOException {

		ListSourceContent lsc = new ListSourceContent();

		lsc.setContent(inputs);


		if(indexCount<pageLength){
			try {
				indexCount++;
				System.out.println("=============DB INGEST COMPLETE for index "+indexCount+" of "+pageLength);
				saveSource(lsc);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("=========================DB INGEST COMPLETE for ALL "+indexCount+" of "+pageLength);
		}


	}


}

