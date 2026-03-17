/*
 * Copyright (C) 2024 The FUTO Keyboard Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.futo.inputmethod.latin;

import android.test.suitebuilder.annotation.LargeTest;
import org.futo.inputmethod.latin.common.Constants;

/**
 * Unit tests for the arrow conversion feature.
 * Tests automatic conversion of "- >" to " -> " when typing.
 */
@LargeTest
public class ArrowConversionTests extends InputTestsBase {

    /**
     * Test that typing "- >" automatically converts to " -> "
     */
    public void testDashSpaceGreaterThanConvertsToArrow() {
        final String WORD_TO_TYPE = "hello";
        final String SEPARATOR = "-";
        final String ARROW_TRIGGER = ">";
        final String EXPECTED_RESULT = "hello -> ";

        type(WORD_TO_TYPE);
        pickSuggestionManually(WORD_TO_TYPE);
        type(SEPARATOR);
        type(ARROW_TRIGGER);

        assertEquals("dash space greater-than should convert to arrow",
                EXPECTED_RESULT, mEditText.getText().toString());
    }

    /**
     * Test arrow conversion works at the beginning of text
     */
    public void testArrowConversionAtStartOfText() {
        final String SEPARATOR = "-";
        final String ARROW_TRIGGER = ">";

        type(SEPARATOR);
        type(ARROW_TRIGGER);

        final String result = mEditText.getText().toString();
        assertTrue("arrow conversion should work at start of text (got: " + result + ")",
                result.contains("->"));
    }

    /**
     * Test multiple arrow conversions in sequence
     */
    public void testMultipleArrowConversions() {
        final String WORD1 = "a";
        final String WORD2 = "b";
        final String SEPARATOR = "-";
        final String ARROW_TRIGGER = ">";

        type(WORD1);
        pickSuggestionManually(WORD1);
        type(SEPARATOR);
        type(ARROW_TRIGGER);
        type(" ");
        type(WORD2);
        pickSuggestionManually(WORD2);
        type(SEPARATOR);
        type(ARROW_TRIGGER);

        final String result = mEditText.getText().toString();
        assertTrue("multiple arrow conversions (got: " + result + ")",
                result.contains("->"));
    }

    /**
     * Test that ">" without preceding "- " doesn't trigger conversion
     */
    public void testNoConversionWithoutDash() {
        final String WORD = "hello";
        final String CHAR = ">";

        type(WORD);
        pickSuggestionManually(WORD);
        type(CHAR);

        final String result = mEditText.getText().toString();
        assertTrue("greater-than without dash should not convert (got: " + result + ")",
                result.contains(">") && !result.contains("->"));
    }

    /**
     * Test that "- x" (dash space x) doesn't trigger conversion
     */
    public void testNoConversionWithWrongSequence() {
        final String WORD = "test";
        final String SEPARATOR = "-";
        final String OTHER_CHAR = "x";

        type(WORD);
        pickSuggestionManually(WORD);
        type(SEPARATOR);
        type(OTHER_CHAR);

        final String result = mEditText.getText().toString();
        assertFalse("wrong sequence should not convert to arrow (got: " + result + ")",
                result.contains("->x"));
    }

    /**
     * Test that backspace after arrow conversion works correctly
     */
    public void testBackspaceAfterArrowConversion() {
        final String WORD = "hello";
        final String SEPARATOR = "-";
        final String ARROW_TRIGGER = ">";

        type(WORD);
        pickSuggestionManually(WORD);
        type(SEPARATOR);
        type(ARROW_TRIGGER);
        type(Constants.CODE_DELETE);

        final String result = mEditText.getText().toString();
        assertTrue("backspace after arrow conversion should leave arrow (got: " + result + ")",
                result.equals("hello ->"));
    }

    /**
     * Test that typing continues normally after arrow conversion
     */
    public void testArrowConversionThenContinueTyping() {
        final String WORD1 = "func";
        final String SEPARATOR = "-";
        final String ARROW_TRIGGER = ">";
        final String WORD2 = "result";

        type(WORD1);
        pickSuggestionManually(WORD1);
        type(SEPARATOR);
        type(ARROW_TRIGGER);
        type(WORD2);

        final String result = mEditText.getText().toString();
        assertTrue("should contain arrow (got: " + result + ")",
                result.contains("->"));
        assertTrue("should continue typing after arrow (got: " + result + ")",
                result.contains("result"));
    }

    /**
     * Test that arrow appears correctly in the middle of text
     */
    public void testArrowConversionInMiddleOfText() {
        final String WORD1 = "start";
        final String WORD2 = "end";
        final String SEPARATOR = "-";
        final String ARROW_TRIGGER = ">";

        type(WORD1);
        pickSuggestionManually(WORD1);
        type(SEPARATOR);
        type(ARROW_TRIGGER);
        type(" ");
        type(WORD2);

        final String result = mEditText.getText().toString();
        assertTrue("arrow should appear in middle of text (got: " + result + ")",
                result.contains("->"));
        assertTrue("should have both words (got: " + result + ")",
                result.contains("start") && result.contains("end"));
    }
}
