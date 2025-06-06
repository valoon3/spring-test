package com.app.springtest.junitTest.ch6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchSetTest {
    private Criteria criteria;
    private Question questionReimbursesTuition;
    private Question questionIsThereRelocation;
    private Question questionOnsiteDaycare;

    private Answer answerDoesNotReimburseTuition;
    private Answer answerReimbursesTuition;
    private Answer answerThereIsRelocation;
    private Answer answerThereIsNoRelocation;
    private Answer answerHasOnsiteDaycare;
    private Answer answerNoOnsiteDaycare;


    private Map<String, Answer> answers;

    @BeforeEach
    public void init() {
        answers = new HashMap<>();
        criteria = new Criteria();

        questionIsThereRelocation = new BooleanQuestion(1, "Relocation package?");
        answerThereIsRelocation = new Answer(questionIsThereRelocation, Bool.TRUE);
        answerThereIsNoRelocation = new Answer(questionIsThereRelocation, Bool.FALSE);

        questionReimbursesTuition = new BooleanQuestion(1, "Reimburses tuition?");
        answerReimbursesTuition = new Answer(questionReimbursesTuition, Bool.TRUE);
        answerDoesNotReimburseTuition = new Answer(questionReimbursesTuition, Bool.FALSE);

        questionOnsiteDaycare = new BooleanQuestion(1, "Onsite daycare?");
        answerHasOnsiteDaycare = new Answer(questionOnsiteDaycare, Bool.TRUE);
        answerNoOnsiteDaycare = new Answer(questionOnsiteDaycare, Bool.FALSE);
    }

    @Test
    public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
        add(answerDoesNotReimburseTuition);
        criteria.add(new Criterion(answerReimbursesTuition, Weight.MustMatch));

        assertFalse(createMatchSet().getMatches());
    }

    @Test
    public void matchAnswersTrueForAnyDontCareCriteria() {
        add(answerDoesNotReimburseTuition);
        criteria.add(new Criterion(answerReimbursesTuition, Weight.DontCare));

        assertTrue(createMatchSet().getMatches());
    }



    private void add(Answer answer) {
        answers.put(answer.getQuestionText(), answer);
    }

    private MatchSet createMatchSet() {
        return new MatchSet(answers, criteria);
    }
}
