package client;

import java.util.HashMap;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoanpreApprovalDMNRuleTest {

    private static final Logger log = LoggerFactory.getLogger(LoanpreApprovalDMNRuleTest.class);

	public static final void main(String[] args) {
		try {
			// Identify the project in the local repository:
			ReleaseId rid = new ReleaseIdImpl("com.creditsuisse", "loanpreapprovaldmn", "1.0.0-SNAPSHOT");
			// Load the KIE base:
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.newKieContainer(rid);
			
			DMNRuntime dmnRuntime =
					KieRuntimeFactory.of(kContainer.getKieBase()).get(DMNRuntime.class);
			String namespace = "https://kiegroup.org/dmn/_515ECC75-150F-4814-9E39-466584FD0221";
			String modelName = "loanpreapprovaldmn";
			DMNModel dmnModel = dmnRuntime.getModel(namespace, modelName);
			DMNContext dmnContext = dmnRuntime.newContext();
			
        	dmnContext.set("CreditScore", 670);
        	HashMap<String, Object> hs = new HashMap<String, Object>();
        	hs.put("monthlyIncome", 50000);
            hs.put("additionalExpenses", 10000);
        	dmnContext.set("Applicant",  hs);
        	
        	HashMap<String, Object> ls = new HashMap<String, Object>();
        	ls. put("interestRate", 3);
        	ls.put("principal", 200000);
        	ls.put("term", 60);
        	dmnContext.set("Loan",ls);
        	
        	DMNResult dmnResult =
        			dmnRuntime.evaluateAll(dmnModel, dmnContext); 
			for (DMNDecisionResult dr : dmnResult.getDecisionResults()) {
				log.info("Decision: '" + dr.getDecisionName() + "', " + "Result: "
						+ dr.getResult());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
