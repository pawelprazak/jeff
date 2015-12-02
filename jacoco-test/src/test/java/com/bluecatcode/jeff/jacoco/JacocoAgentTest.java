package com.bluecatcode.jeff.jacoco;

import org.jacoco.agent.rt.internal_b0d6a23.Agent;
import org.jacoco.agent.rt.internal_b0d6a23.core.runtime.AgentOptions;
import org.jacoco.core.data.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JacocoAgentTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private AgentOptions options;
    private File execfile;

    private Agent agent;

    @Before
    public void setup() {
        options = new AgentOptions();
        execfile = new File(folder.getRoot(), "jacoco.exec");
        options.setOutput(AgentOptions.OutputMode.file);
        options.setDestfile(execfile.getAbsolutePath());
        options.setSessionId("testsession");
        agent = Agent.getInstance(options);
        agent.startup();
    }

    @After
    public void tearDown() throws Exception {
        agent.shutdown();

        assertTrue(execfile.isFile());
        assertTrue(execfile.length() > 0);

        SessionInfoStore sessionStore = new SessionInfoStore();
        ExecutionDataStore executionDataStore = new ExecutionDataStore();
        ExecutionDataReader reader = new ExecutionDataReader(new ByteArrayInputStream(agent.getExecutionData(false)));
        reader.setSessionInfoVisitor(sessionStore);
        reader.setExecutionDataVisitor(executionDataStore);
        reader.read();

        for (ExecutionData executionData : executionDataStore.getContents()) {
            System.out.println(executionData.toString());
        }
    }

    @Test
    public void testName() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("1", "2");

        System.out.println(map);
    }
}
