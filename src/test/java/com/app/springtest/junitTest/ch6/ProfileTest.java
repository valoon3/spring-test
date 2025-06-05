package com.app.springtest.junitTest.ch6;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfileTest {

    private Profile profile;
    private BooleanQuestion question;
    private Criteria criteria;

    @BeforeEach
    void create() {
        profile = new Profile("Bull Hockey, Inc.");
        question = new BooleanQuestion(1, "Got bonuses?");
        criteria = new Criteria();
    }

    @Test
    public void 답변이_FALSE_일때_Criteria__NotMet_이다() {
        profile.add(new Answer(question, Bool.FALSE));
        criteria.add(new Criterion(
                        new Answer(question, Bool.TRUE),
                        Weight.MustMatch
                )
        );

        boolean matches = profile.matches(criteria);

        assertFalse(matches);
    }

    @Test
    public void 일치하는_답변_기준에_대해_true() {
        profile.add(new Answer(question, Bool.FALSE));
        criteria.add(new Criterion(
                        new Answer(question, Bool.TRUE),
                        Weight.DontCare
                )
        );

        boolean matches = profile.matches(criteria);
        assertTrue(matches);
    }
}