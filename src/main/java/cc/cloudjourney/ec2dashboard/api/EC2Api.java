package cc.cloudjourney.ec2dashboard.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.AssociateIamInstanceProfileRequest;
import com.amazonaws.services.ec2.model.AssociateIamInstanceProfileResult;
import com.amazonaws.services.ec2.model.DescribeIamInstanceProfileAssociationsRequest;
import com.amazonaws.services.ec2.model.DescribeIamInstanceProfileAssociationsResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DisassociateIamInstanceProfileRequest;
import com.amazonaws.services.ec2.model.IamInstanceProfileAssociation;
import com.amazonaws.services.ec2.model.IamInstanceProfileSpecification;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

import cc.cloudjourney.ec2dashboard.model.EC2;
import cc.cloudjourney.ec2dashboard.model.Role;

public class EC2Api {
	private static final Map<String, EC2> ec2instances = new HashMap<String, EC2>();

	public static List<EC2> list(String q) {
		ec2instances.clear();
		DescribeInstancesResult instanceResults = AmazonEC2ClientBuilder.defaultClient().describeInstances();
		List<Reservation> rs = instanceResults.getReservations();
		if (!rs.isEmpty()) {
			for (Reservation tmp : rs) {
    			List<Instance> instances = tmp.getInstances();
    			for (Instance i : instances) {
    				String instanceID = i.getInstanceId();
    				String name = EC2.getInstanceName(i);
					if (i.getIamInstanceProfile() != null) {
						ec2instances.put(instanceID, new EC2(name, instanceID, new Role(i.getIamInstanceProfile())));
					} else {
						ec2instances.put(instanceID, new EC2(name, instanceID, null));
					}
    			}
			}
		}
		Collection<EC2> instances = ec2instances.values();
		
		if (q != null && q.length() > 2) {
			List<EC2> result = new ArrayList<EC2>();
			for (EC2 tmp : instances) { 
				if (tmp.getName().contains(q)) {
					result.add(tmp);
				}
			}
			return result;
		}
		return new ArrayList<EC2>(instances);
	}

	public static String update(String instanceID, String roleName) {
		AmazonEC2 client = AmazonEC2ClientBuilder.defaultClient();
		
		//Clean everything
		DescribeIamInstanceProfileAssociationsRequest descRequest = new DescribeIamInstanceProfileAssociationsRequest();
		DescribeIamInstanceProfileAssociationsResult descResults = client.describeIamInstanceProfileAssociations(descRequest);
		
		List<IamInstanceProfileAssociation> results = descResults.getIamInstanceProfileAssociations();
		if (results != null && !results.isEmpty()) {
			for (IamInstanceProfileAssociation association : results) {
				if (association.getInstanceId().equals(instanceID)) {
					DisassociateIamInstanceProfileRequest disRequest = new DisassociateIamInstanceProfileRequest();
					disRequest.setAssociationId(association.getAssociationId());
					client.disassociateIamInstanceProfile(disRequest);
				}
			}
		}

		AssociateIamInstanceProfileRequest request = new AssociateIamInstanceProfileRequest();
		request.setInstanceId(instanceID);
		//Associate!!!
		IamInstanceProfileSpecification profile = new IamInstanceProfileSpecification();
		profile.setName(roleName);
		request.setIamInstanceProfile(profile);
		AssociateIamInstanceProfileResult result = client.associateIamInstanceProfile(request);
		return result.getIamInstanceProfileAssociation().getState();
	}
	
	public static String deleteRole(String instanceID) {
		AmazonEC2 client = AmazonEC2ClientBuilder.defaultClient();
		
		//Clean everything
		DescribeIamInstanceProfileAssociationsRequest descRequest = new DescribeIamInstanceProfileAssociationsRequest();
		DescribeIamInstanceProfileAssociationsResult descResults = client.describeIamInstanceProfileAssociations(descRequest);
		
		List<IamInstanceProfileAssociation> results = descResults.getIamInstanceProfileAssociations();
		if (results != null && !results.isEmpty()) {
			for (IamInstanceProfileAssociation association : results) {
				if (association.getInstanceId().equals(instanceID)) {
					DisassociateIamInstanceProfileRequest disRequest = new DisassociateIamInstanceProfileRequest();
					disRequest.setAssociationId(association.getAssociationId());
					client.disassociateIamInstanceProfile(disRequest);
				}
			}
		}

		return "disassociated";
	}
}
