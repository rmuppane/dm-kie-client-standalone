package client;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.loanapprovaldrl.LoanApplication;

public class DRLRuleTest {

    private static final Logger log = LoggerFactory.getLogger(DRLRuleTest.class);

	public static final void main(String[] args) {
		try {
			// Identify the project in the local repository:
			ReleaseId rid = new ReleaseIdImpl("com.cs", "LoanApprovalDRL", "1.0.0-SNAPSHOT");
			// Load the KIE base:
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.newKieContainer(rid);
			KieSession kSession = kContainer.newKieSession();
			
			// Set up the fact model:
			LoanApplication loanApp = new LoanApplication();
        	loanApp.setCreditScore(800);
        	loanApp.setDti(0.41f);
        	
			// Insert the person into the session:
			kSession.insert(loanApp);
			
			// Fire all rules:
			kSession.fireAllRules();
			kSession.dispose();
			System.out.println("Approval status [ " + loanApp.getLoanApproval() + "]");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
