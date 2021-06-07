package client;

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

public class DMNRuleTest {

    private static final Logger log = LoggerFactory.getLogger(DMNRuleTest.class);

	public static final void main(String[] args) {
		try {
			// Identify the project in the local repository:
			ReleaseId rid = new ReleaseIdImpl("com.cs", "LoanApprovalDMN", "1.0.0-SNAPSHOT");
			// Load the KIE base:
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.newKieContainer(rid);
			
			DMNRuntime dmnRuntime =
					KieRuntimeFactory.of(kContainer.getKieBase()).get(DMNRuntime.class);
			String namespace = "https://kiegroup.org/dmn/_6E1E1D1D-5D41-4A33-8838-206C1E7B3584";
			String modelName = "loanapproval";
			DMNModel dmnModel = dmnRuntime.getModel(namespace, modelName);
			DMNContext dmnContext = dmnRuntime.newContext();
			
			dmnContext.set("CreditScore", 800);
        	dmnContext.set("DTI", 0.41f);
        	
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
