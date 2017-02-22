package cc.cloudjourney.ec2dashboard.application;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.util.Collections;

import org.eclipse.jetty.util.log.Log;

import cc.cloudjourney.ec2dashboard.api.EC2Api;
import cc.cloudjourney.ec2dashboard.api.RoleApi;
import spark.Spark;

public class Application {
	
    public static void main(String[] args) {
    	staticFiles.location("/web");
  
    	
    	path("/api", () -> {
    	    path("/ec2", () -> {
    	    	//list and search
    	    	get("", "application/json", (req, res) -> {
    	        	res.type("application/json");
    	    		String q = req.queryMap("q").value();
    	    		return EC2Api.list(q);
    	    	}, new JsonTransformer());
    	    	get("/", "application/json", (req, res) -> {
    	        	res.type("application/json");
    	    		String q = req.queryMap("q").value();
    	    		return EC2Api.list(q);
    	    	}, new JsonTransformer());

    	    	delete("/:instanceid/", "application/json", (req, res) -> {
    	    		Log.getLog().info("DELETE ::: DELETE INSTANCE ::: " + req.params(":instanceid"));
    	    		String result = EC2Api.deleteRole(req.params(":instanceid"));
    	    		
    	        	res.type("application/json");
    	    		return Collections.singletonMap("result", String.format("Instance [%s] and Profile are %s", req.params(":instanceid"), result));

    	    	}, new JsonTransformer()); //associate
    	    	
    	    	post("/:instanceid/:rolename", "application/json", (req, res) -> {
    	    		Log.getLog().info("POST ::: ASSOCIATE INSTANCE ::: " + req.params(":instanceid") + " AND ROLE ::: " + req.params(":rolename"));
    	    		String result = EC2Api.update(req.params(":instanceid"), req.params(":rolename"));
    	    		
    	        	res.type("application/json");
    	    		return Collections.singletonMap("result", String.format("Instance [%s] and Profile [%s] are %s", req.params(":instanceid"), req.params(":rolename"), result));

    	    	}, new JsonTransformer()); //associate
    	    });

    	    path("/iam", () -> {
    	        get("/roles", "application/json", (req, res) -> {
    	        	res.type("application/json");
    	    		String q = req.queryMap("q").value();
    	    		return RoleApi.list(q);
    	        }, new JsonTransformer()); //list all roles
    	        get("/roles/", "application/json", (req, res) -> {
    	        	res.type("application/json");
    	    		String q = req.queryMap("q").value();
    	    		return RoleApi.list(q);
    	        }, new JsonTransformer()); //list all roles
    	    });
    	});

    	Spark.exception(Exception.class, (exception, request, response) -> {
    	    exception.printStackTrace();
    	});
    }
}
