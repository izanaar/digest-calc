package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.exception.JobServiceException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecursiveActionExecutionServiceTest {

    @Mock
    private ForkJoinPool pool;

    @Mock
    private HashMap<Long, RecursiveAction> actions;

    @InjectMocks
    private RecursiveActionExecutionService executionService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(executionService, "actionMap",actions);
    }

    @Test
    public void executeAction() throws Exception {
        RecursiveAction action = mock(RecursiveAction.class);
        Long id = 2L;

        executionService.executeAction(action, id);
        verify(actions).put(id, action);
        verify(pool).execute(action);
    }

    @Test
    public void testCancelActionExecutuion() throws Exception {
        RecursiveAction action = mock(RecursiveAction.class);
        Long id = 55L;

        when(actions.get(id)).thenReturn(action);
        when(action.cancel(true)).thenReturn(true);

        assertTrue(executionService.tryCancelActionExecutuion(id));
    }

    @Test
    public void testFailedCancelActionExecution() throws Exception {
        Long id = 1L;
        thrown.expect(JobServiceException.class);
        thrown.expectMessage("Couldn't cancel job. Invalid id: " + id);
        executionService.tryCancelActionExecutuion(id);
    }
}