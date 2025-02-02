package tests;

import org.alessio29.savagebot.r2.eval.CommandContext;
import org.alessio29.savagebot.r2.eval.Interpreter;
import org.alessio29.savagebot.r2.parse.Parser;
import org.alessio29.savagebot.r2.tree.Statement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class TestR2Interpreter {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Test
    public void testRandomText() {
        expect(
                "No commands",
                "ha ha ha"
        );
        expect(
                "bla bla bla 1234 = **1234**",
                "bla", "bla", "bla", "1234"
        );
    }

    @Test
    public void testArithmetic() {
        expect(
                "2 + 2 = **4**",
                "2+2"
        );
        expect(
                "2 + 2 * 2 = **6**",
                "2+2*2"
        );
        expect(
                "1/0: Division by 0",
                "1/0"
        );
        expect(
                "1%0: Division by 0",
                "1%0"
        );
    }

    @Test
    public void testGenericRolls() {
        expect(
                "{2d6: [1,5]} = **6**",
                "2d6"
        );
        expect(
                "{10d6!: [1,5,2,6+6+6+6+4=28,4,3,6+6+6+5=23,6+3=9,5,4]} = **84**",
                "10d6!"
        );
        expect(
                "{4d6k3: [~~1~~,2,5,6]} = **13**",
                "4d6k3"
        );
        expect(
                "{3d6: [1,5,2]} + {2d4: [3,3]} = **14**",
                "3d6+2d4"
        );
        expect(
                "{d20adv: [~~1~~,9]} = **9**",
                "d20adv"
        );
        expect(
                "{d20adv1: [~~1~~,9]} = **9**",
                "d20adv1"
        );
        expect(
                "{d20dis: [1,~~9~~]} = **1**",
                "d20dis"
        );
        expect(
                "{d20dis1: [1,~~9~~]} = **1**",
                "d20dis1"
        );
    }

    @Test
    public void testFudgeRolls() {
        expect(
                "{df: [-..+]} = **0**",
                "df"
        );
        expect(
                "{10DF: [-..++++--+]} = **2**",
                "10DF"
        );
        expect(
                "{3d6: [1,5,2]} + {df: [++++]} = **12**",
                "3d6+df"
        );
    }

    @Test
    public void testSavageWorldsRolls() {
        expect(
                "{s8: [wild: 5; 6]} = **6**",
                "s8"
        );
        expect(
                "{s8: [wild: 5; 6]} + 2 = **8**",
                "s8+2"
        );
        expect(
                "{s8: [wild: 5; 6]} + {2df: [.+]} = **7**",
                "s8+2df"
        );
        expect(
                "{3s8: [wild: 6+6+6+6+4=28; 2; 6; 7]} = **6**, **7**, **28**",
                "3s8"
        );
        expect(
                "{3s8w10: [wild: 8; 2; 6; 7]} = **6**, **7**, **8**",
                "3s8w10"
        );
        expect(
                "{3s6: [wild: 6+6+6+6+4=28; 1; 2; 5]} + 2 = **4**, **7**, **30**",
                "3s6+2"
        );
    }

    @Test
    public void testRepeatedRolls() {
        expect(
                "10x2d6: \n" +
                        "1: {2d6: [1,5]} = **6**\n" +
                        "2: {2d6: [2,6]} = **8**\n" +
                        "3: {2d6: [6,6]} = **12**\n" +
                        "4: {2d6: [6,4]} = **10**\n" +
                        "5: {2d6: [4,3]} = **7**\n" +
                        "6: {2d6: [6,6]} = **12**\n" +
                        "7: {2d6: [6,5]} = **11**\n" +
                        "8: {2d6: [6,3]} = **9**\n" +
                        "9: {2d6: [5,4]} = **9**\n" +
                        "10: {2d6: [6,3]} = **9**",
                "10x2d6"
        );
        expect(
                "10x3s6+2: \n" +
                        "1: {3s6: [wild: 6+6+6+6+4=28; 1; 2; 5]} + 2 = **4**, **7**, **30**\n" +
                        "2: {3s6: [wild: 6+3=9; 3; 4; 6+6+6+5=23]} + 2 = **6**, **11**, **25**\n" +
                        "3: {3s6: [wild: 2; 4; 5; 6+3=9]} + 2 = **6**, **7**, **11**\n" +
                        "4: {3s6: [wild: 5; 1; 3; 6+3=9]} + 2 = **5**, **7**, **11**\n" +
                        "5: {3s6: [wild: 6+6+6+6+6+3=33; 1; 4; 5]} + 2 = **6**, **7**, **35**\n" +
                        "6: {3s6: [wild: 4; 3; 4; 6+5=11]} + 2 = **6**, **6**, **13**\n" +
                        "7: {3s6: [wild: 5; 2; 3; 6+3=9]} + 2 = **5**, **7**, **11**\n" +
                        "8: {3s6: [wild: 5; 4; 5; 5]} + 2 = **7**, **7**, **7**\n" +
                        "9: {3s6: [wild: 3; 1; 2; 4]} + 2 = **4**, **5**, **6**\n" +
                        "10: {3s6: [wild: 5; 1; 4; 5]} + 2 = **6**, **7**, **7**",
                "10x3s6+2"
        );
    }

    @Test
    public void testRollsWithRollsInRolls() {
        expect(
                "{(d6)d6: [5]} = **5**",
                "(d6)d6"
        );
        expect(
                "{(d4+2)d6: [5,2,6,6,6]} = **25**",
                "(d4+2)d6"
        );
        expect(
                "(2d6)x2d6: ({2d6: [1,5]}) = **6**\n" +
                        "1: {2d6: [2,6]} = **8**\n" +
                        "2: {2d6: [6,6]} = **12**\n" +
                        "3: {2d6: [6,4]} = **10**\n" +
                        "4: {2d6: [4,3]} = **7**\n" +
                        "5: {2d6: [6,6]} = **12**\n" +
                        "6: {2d6: [6,5]} = **11**",
                "(2d6)x2d6"
        );
    }

    @Test
    public void testCommented() {
        expect(
                "{d20: [1]} + (STR: 3) + (Attack bonus: 5) = **9**",
                "d20+(\"STR\" 3)+(\"Attack bonus\" 5)"
        );
    }

    @Test
    public void testRollBatchRepeated() {
        expect(
                "10x[d100 2d6+1 2d6]: \n" +
                        "1: {d100: [61]} = **61**; {2d6: [5,2]} + 1 = **8**; {2d6: [6,6]} = **12**; \n" +
                        "2: {d100: [54]} = **54**; {2d6: [6,4]} + 1 = **11**; {2d6: [4,3]} = **7**; \n" +
                        "3: {d100: [78]} = **78**; {2d6: [6,6]} + 1 = **13**; {2d6: [5,6]} = **11**; \n" +
                        "4: {d100: [45]} = **45**; {2d6: [5,4]} + 1 = **10**; {2d6: [6,3]} = **9**; \n" +
                        "5: {d100: [44]} = **44**; {2d6: [1,3]} + 1 = **5**; {2d6: [6,3]} = **9**; \n" +
                        "6: {d100: [61]} = **61**; {2d6: [4,5]} + 1 = **10**; {2d6: [1,6]} = **7**; \n" +
                        "7: {d100: [46]} = **46**; {2d6: [6,6]} + 1 = **13**; {2d6: [6,3]} = **9**; \n" +
                        "8: {d100: [63]} = **63**; {2d6: [4,6]} + 1 = **11**; {2d6: [5,4]} = **9**; \n" +
                        "9: {d100: [61]} = **61**; {2d6: [2,6]} + 1 = **9**; {2d6: [3,5]} = **8**; \n" +
                        "10: {d100: [92]} = **92**; {2d6: [5,5]} + 1 = **11**; {2d6: [5,1]} = **6**;",
                "10x[d100 2d6+1 2d6]"
        );
        expect(
                "(2d6)x[d100 2d6+1 2d6]: ({2d6: [1,5]}) = **6**\n" +
                        "1: {d100: [30]} = **30**; {2d6: [6,6]} + 1 = **13**; {2d6: [6,6]} = **12**; \n" +
                        "2: {d100: [62]} = **62**; {2d6: [4,3]} + 1 = **8**; {2d6: [6,6]} = **12**; \n" +
                        "3: {d100: [74]} = **74**; {2d6: [5,6]} + 1 = **12**; {2d6: [3,5]} = **8**; \n" +
                        "4: {d100: [76]} = **76**; {2d6: [6,3]} + 1 = **10**; {2d6: [2,1]} = **3**; \n" +
                        "5: {d100: [25]} = **25**; {2d6: [6,3]} + 1 = **10**; {2d6: [5,4]} = **9**; \n" +
                        "6: {d100: [83]} = **83**; {2d6: [1,6]} + 1 = **8**; {2d6: [6,6]} = **12**;",
                "(2d6)x[d100 2d6+1 2d6]"
        );
        expect(
                "10x[\"rating\" d100 \"weight\" 2d6+1 2d6]: \n" +
                        "1: rating: {d100: [61]} = **61**; weight: {2d6: [5,2]} + 1 = **8**; {2d6: [6,6]} = **12**; \n" +
                        "2: rating: {d100: [54]} = **54**; weight: {2d6: [6,4]} + 1 = **11**; {2d6: [4,3]} = **7**; \n" +
                        "3: rating: {d100: [78]} = **78**; weight: {2d6: [6,6]} + 1 = **13**; {2d6: [5,6]} = **11**; \n" +
                        "4: rating: {d100: [45]} = **45**; weight: {2d6: [5,4]} + 1 = **10**; {2d6: [6,3]} = **9**; \n" +
                        "5: rating: {d100: [44]} = **44**; weight: {2d6: [1,3]} + 1 = **5**; {2d6: [6,3]} = **9**; \n" +
                        "6: rating: {d100: [61]} = **61**; weight: {2d6: [4,5]} + 1 = **10**; {2d6: [1,6]} = **7**; \n" +
                        "7: rating: {d100: [46]} = **46**; weight: {2d6: [6,6]} + 1 = **13**; {2d6: [6,3]} = **9**; \n" +
                        "8: rating: {d100: [63]} = **63**; weight: {2d6: [4,6]} + 1 = **11**; {2d6: [5,4]} = **9**; \n" +
                        "9: rating: {d100: [61]} = **61**; weight: {2d6: [2,6]} + 1 = **9**; {2d6: [3,5]} = **8**; \n" +
                        "10: rating: {d100: [92]} = **92**; weight: {2d6: [5,5]} + 1 = **11**; {2d6: [5,1]} = **6**;",
                "10x[\"rating\" d100 \"weight\" 2d6+1 2d6]"
        );
    }

    @Test
    public void testD66() {
        expect(
                "{d66: [1,5]} = **15**",
                "d66"
        );
        expect(
                "{d666: [1,5,2]} = **152**",
                "d666"
        );
        expect(
                "{d6666: [1,5,2,6]} = **1526**",
                "d6666"
        );
    }

    @Test
    public void testShootingAtVampireExample() {
        expect(
                "shooting at vampire {s8: [wild: 5; 6]} = **6**\n" +
                        "damage {2d6: [2,6]} + 1 = **9**",
                "shooting", "at", "vampire", "s8", "damage", "2d6+1"
        );
    }

    @Test
    public void testDebugMode() {
        expect(
                "*Debug mode enabled.*\n" +
                        "\n" +
                        "`shooting`:\n" +
                        "```\n" +
                        "NonParsedString text='shooting' parserErrorMessage='[1]: token recognition error at: 'h''\n" +
                        "```\n" +
                        "shooting \n" +
                        "`2d6`:\n" +
                        "```\n" +
                        "RollOnce\n" +
                        "  expr: GenericRoll isOpenEnded=false\n" +
                        "    diceCount: Int 2\n" +
                        "    facetsCount: Int 6\n" +
                        "    suffixArg1: null\n" +
                        "    suffixArg2: null\n" +
                        "```\n" +
                        "{2d6: [1,5]} = **6**",
                "--debug", "shooting", "2d6"
        );
    }

    @Test
    public void testSuccessOrFail() {
        expect(
                "{12d10s7: [1, 9:white_check_mark:, 10:white_check_mark:, 8:white_check_mark:, 6, 4, 2, 2, " +
                        "10:white_check_mark:, 5, 8:white_check_mark:, 8:white_check_mark:]} = **6**",
                "12d10s7"
        );
        expect(
                "{12d10s7f1: [1:red_circle:, 9:white_check_mark:, 10:white_check_mark:, 8:white_check_mark:, " +
                        "6, 4, 2, 2, 10:white_check_mark:, 5, 8:white_check_mark:, 8:white_check_mark:]} = **5**",
                "12d10s7f1"
        );
        expect(
                "{12d10f1s7: [1:red_circle:, 9:white_check_mark:, 10:white_check_mark:, " +
                        "8:white_check_mark:, 6, 4, 2, 2, 10:white_check_mark:, 5, 8:white_check_mark:, " +
                        "8:white_check_mark:]} = **5**",
                "12d10f1s7"
        );
        expect(
                "{28d6!s10f1: [1:red_circle:, 5, 2, 6+6+6+6+4=28:white_check_mark:, 4, 3, " +
                        "6+6+6+5=23:white_check_mark:, 6+3=9, 5, 4, 6+3=9, 2, 1:red_circle:, 3, 6+3=9, 5, 4, 5, " +
                        "1:red_circle:, 6+6+6+6+6+3=33:white_check_mark:, 3, 4, 6+5=11:white_check_mark:, " +
                        "4, 3, 2, 6+3=9, 5]} = **1**",
                "28d6!s10f1"
        );
    }

    @Test
    public void testBoundTo() {
        expect(
                "5x5d10[20:30]: \n" +
                        "1: {5d10[20:30]: [1,9,10,8,6] 34=>30} = **30**\n" +
                        "2: {5d10[20:30]: [4,2,2,10,5] 23} = **23**\n" +
                        "3: {5d10[20:30]: [8,8,4,3,6] 29} = **29**\n" +
                        "4: {5d10[20:30]: [5,5,6,2,1] 19=>20} = **20**\n" +
                        "5: {5d10[20:30]: [4,9,5,8,3] 29} = **29**",
                "5x5d10[20:30]"
        );
        expect(
                "5x5d10[20:]: \n" +
                        "1: {5d10[20:]: [1,9,10,8,6] 34} = **34**\n" +
                        "2: {5d10[20:]: [4,2,2,10,5] 23} = **23**\n" +
                        "3: {5d10[20:]: [8,8,4,3,6] 29} = **29**\n" +
                        "4: {5d10[20:]: [5,5,6,2,1] 19=>20} = **20**\n" +
                        "5: {5d10[20:]: [4,9,5,8,3] 29} = **29**",
                "5x5d10[20:]"
        );
        expect(
                "5x5d10[:30]: \n" +
                        "1: {5d10[:30]: [1,9,10,8,6] 34=>30} = **30**\n" +
                        "2: {5d10[:30]: [4,2,2,10,5] 23} = **23**\n" +
                        "3: {5d10[:30]: [8,8,4,3,6] 29} = **29**\n" +
                        "4: {5d10[:30]: [5,5,6,2,1] 19} = **19**\n" +
                        "5: {5d10[:30]: [4,9,5,8,3] 29} = **29**",
                "5x5d10[:30]"
        );
        expect(
                "Error: `5x5d10[:]`: At least one bound should be provided: `5d10[:]`",
                "5x5d10[:]"
        );
        expect(
                "Error: `5d10[30:20]`: Empty range: `5d10[30:20]`",
                "5d10[30:20]"
        );
        expect(
                "5d10[10+20:20]: Empty range in `5d10[10+20:20]`: [30:20]",
                "5d10[10+20:20]"
        );
        expect(
                "{(d10!+d8!+d6!)[8:10+8+6]: ({d10!: [1]} + {d8!: [7]} + {d6!: [2]}) 10} = **10**",
                "(d10!+d8!+d6!)[8:10+8+6]"
        );
        expect(
                "{(d10!+d8!+d6!)[8:10+8+6]: ({d10!: [1]} + {d8!: [7]} + {d6!: [2]}) 10} + 4 = **14**",
                "(d10!+d8!+d6!)[8:10+8+6]+4"
        );
        expect(
                "5x(d10!+d8!+d6!)[8:10+8+6]: \n" +
                        "1: {(d10!+d8!+d6!)[8:10+8+6]: ({d10!: [1]} + {d8!: [7]} + {d6!: [2]}) 10} = **10**\n" +
                        "2: {(d10!+d8!+d6!)[8:10+8+6]: ({d10!: [8]} + {d8!: [6]} + {d6!: [6+6+4=16]}) 30=>24} = **24**\n" +
                        "3: {(d10!+d8!+d6!)[8:10+8+6]: ({d10!: [10+5=15]} + {d8!: [3]} + {d6!: [6+6+5=17]}) 35=>24} = **24**\n" +
                        "4: {(d10!+d8!+d6!)[8:10+8+6]: ({d10!: [6]} + {d8!: [8+8+1=17]} + {d6!: [6+3=9]}) 32=>24} = **24**\n" +
                        "5: {(d10!+d8!+d6!)[8:10+8+6]: ({d10!: [4]} + {d8!: [3]} + {d6!: [3]}) 10} = **10**",
                "5x(d10!+d8!+d6!)[8:10+8+6]"
        );
    }

    @Test
    public void testVariables() {
        expect(
                "{d20: [1]} + {@dd=[4]} = **5**\n" +
                        "{@dd=4} + {d8: [2]} = **6**",
                "d20+@dd:=d5", "@dd+d8"
        );
        expect(
                "10x[d10+@dd:=d5; @dd+d8]: \n" +
                        "1: {d10: [1]} + {@dd=[4]} = **5**; {@dd=4} + {d8: [2]} = **6**; \n" +
                        "2: {d10: [8]} + {@dd=[1]} = **9**; {@dd=1} + {d8: [3]} = **4**; \n" +
                        "3: {d10: [2]} + {@dd=[2]} = **4**; {@dd=2} + {d8: [5]} = **7**; \n" +
                        "4: {d10: [5]} + {@dd=[3]} = **8**; {@dd=3} + {d8: [3]} = **6**; \n" +
                        "5: {d10: [4]} + {@dd=[3]} = **7**; {@dd=3} + {d8: [8]} = **11**; \n" +
                        "6: {d10: [5]} + {@dd=[5]} = **10**; {@dd=5} + {d8: [1]} = **6**; \n" +
                        "7: {d10: [2]} + {@dd=[1]} = **3**; {@dd=1} + {d8: [3]} = **4**; \n" +
                        "8: {d10: [9]} + {@dd=[5]} = **14**; {@dd=5} + {d8: [3]} = **8**; \n" +
                        "9: {d10: [3]} + {@dd=[1]} = **4**; {@dd=1} + {d8: [1]} = **2**; \n" +
                        "10: {d10: [3]} + {@dd=[3]} = **6**; {@dd=3} + {d8: [1]} = **4**;",
                "10x[d10+@dd:=d5; @dd+d8]"
        );
        expect(
                "{@n=[3,4]} = **7**\n" +
                        "(@n)x[d20; d4]: ({@n=7}) = **7**\n" +
                        "1: {d20: [10]} = **10**; {d4: [3]} = **3**; \n" +
                        "2: {d20: [16]} = **16**; {d4: [2]} = **2**; \n" +
                        "3: {d20: [12]} = **12**; {d4: [1]} = **1**; \n" +
                        "4: {d20: [20]} = **20**; {d4: [4]} = **4**; \n" +
                        "5: {d20: [18]} = **18**; {d4: [2]} = **2**; \n" +
                        "6: {d20: [14]} = **14**; {d4: [3]} = **3**; \n" +
                        "7: {d20: [16]} = **16**; {d4: [4]} = **4**;",
                "@n:=2d4", "(@n)x[d20; d4]"
        );
    }

    private void expect(String result, String... args) {
        List<Statement> statements = new Parser().parse(args);
        CommandContext context = new CommandContext(new Random(0));
        Interpreter interpreter = new Interpreter(context);
        String actualResult = interpreter.run(statements).trim();
        if (!LINE_SEPARATOR.equals("\n")) {
            actualResult = actualResult
                    .replace(LINE_SEPARATOR, "\n")
                    .replace("\n", LINE_SEPARATOR);
        }
        String expected = result.replace("\n", LINE_SEPARATOR).trim();
        Assert.assertEquals(expected, actualResult);
    }
}
