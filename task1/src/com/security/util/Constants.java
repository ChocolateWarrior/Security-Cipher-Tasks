package com.security.util;

import java.util.Map;

public interface Constants {
    String ANSI_RESET = "\u001B[0m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSI_PURPLE = "\u001B[35m";

    int THREAD_POOL_SIZE = 3;
    int POPULATION_SIZE = 120;
    int MAX_GENERATION_SIZE = 600;
    int TOURNAMENT_SELECTION_SIZE = 120;
    int TOURNAMENT_SELECTION_SIZE_SUBTASK3 = 50;
    int POPULATION_SIZE_SUBTASK3 = 500;
    double CROSSOVER = 0.5;
    double MUTATION = 0.1;
    boolean IS_ELITISM = false;

    double TRIGRAM_WEIGHT = 1.5d;
    double BIGRAM_WEIGHT = 1.25d;
    double MONOGRAM_WEIGHT = 1d;

    int SHARE_PERIOD = 10;

    String SUBTASK1_CIPHERED = "Yx`7cen7v7ergrvc~yp:|rn7OXE7t~g.re97R9p97~c7d.xb{s7cv|r7v7dce~yp75.r{{x7`xe{s57vys;7p~ary7c.r7|rn7~d75|rn5;7oxe7c.r7q~edc7{rccre75.57`~c.75|5;7c.ry7oxe75r57`~c.75r5;7c.ry75{57`~c.75n5;7vys7c.ry7oxe7yroc7t.ve75{57`~c.75|57vpv~y;7c.ry75x57`~c.75r57vys7dx7xy97Nxb7zvn7bdr7vy7~ysro7xq7tx~yt~srytr;7_vzz~yp7s~dcvytr;7\\vd~d|~7rovz~yvc~xy;7dcvc~dc~tv{7crdcd7xe7`.vcrare7zrc.xs7nxb7qrr{7`xb{s7d.x`7c.r7urdc7erdb{c9";
    String SUBTASK2_CIPHERED = "1c41023f564b2a130824570e6b47046b521f3f5208201318245e0e6b40022643072e13183e51183f5a1f3e4702245d4b285a1b23561965133f2413192e571e28564b3f5b0e6b50042643072e4b023f4a4b24554b3f5b0238130425564b3c564b3c5a0727131e38564b245d0732131e3b430e39500a38564b27561f3f5619381f4b385c4b3f5b0e6b580e32401b2a500e6b5a186b5c05274a4b79054a6b67046b540e3f131f235a186b5c052e13192254033f130a3e470426521f22500a275f126b4a043e131c225f076b431924510a295f126b5d0e2e574b3f5c4b3e400e6b400426564b385c193f13042d130c2e5d0e3f5a086b52072c5c192247032613433c5b02285b4b3c5c1920560f6b47032e13092e401f6b5f0a38474b32560a391a476b40022646072a470e2f130a255d0e2a5f0225544b24414b2c410a2f5a0e25474b2f56182856053f1d4b185619225c1e385f1267131c395a1f2e13023f13192254033f13052444476b4a043e131c225f076b5d0e2e574b22474b3f5c4b2f56082243032e414b3f5b0e6b5d0e33474b245d0e6b52186b440e275f456b710e2a414b225d4b265a052f1f4b3f5b0e395689cbaa186b5d046b401b2a500e381d4b23471f3b4051641c0f2450186554042454072e1d08245e442f5c083e5e0e2547442f1c5a0a64123c503e027e040c413428592406521a21420e184a2a32492072000228622e7f64467d512f0e7f0d1a";
    String SUBTASK3_CIPHERED = "EFFPQLEKVTVPCPYFLMVHQLUEWCNVWFYGHYTCETHQEKLPVMSAKSPVPAPVYWMVHQLUSPQLYWLASLFVWPQLMVHQLUPLRPSQLULQESPBLWPCSVRVWFLHLWFLWPUEWFYOTCMQYSLWOYWYETHQEKLPVMSAKSPVPAPVYWHEPPLUWSGYULEMQTLPPLUGUYOLWDTVSQETHQEKLPVPVSMTLEUPQEPCYAMEWWYTYWDLUULTCYWPQLSEOLSVOHTLUYAPVWLYGDALSSVWDPQLNLCKCLRQEASPVILSLEUMQBQVMQCYAHUYKEKTCASLFPYFLMVHQLUPQLHULIVYASHEUEDUEHQBVTTPQLVWFLRYGMYVWMVFLWMLSPVTTBYUNESESADDLSPVYWCYAMEWPUCPYFVIVFLPQLOLSSEDLVWHEUPSKCPQLWAOKLUYGMQEUEMPLUSVWENLCEWFEHHTCGULXALWMCEWETCSVSPYLEMQYGPQLOMEWCYAGVWFEBECPYASLQVDQLUYUFLUGULXALWMCSPEPVSPVMSBVPQPQVSPCHLYGMVHQLUPQLWLRPOEDVMETBYUFBVTTPENLPYPQLWLRPTEKLWZYCKVPTCSTESQPQULLGYAUMEHVPETFWMEHVPETBZMEHVPETB";
    String SUBTASK4_CIPHERED = "KZBWPFHRAFHMFSNYSMNOZYBYLLLYJFBGZYYYZYEKCJVSACAEFLMAJZQAZYHIJFUNHLCGCINWFIHHHTLNVZLSHSVOZDPYSMNYJXHMNODNHPATXFWGHZPGHCVRWYSNFUSPPETRJSIIZSAAOYLNEENGHYAMAZBYSMNSJRNGZGSEZLNGHTSTJMNSJRESFRPGQPSYFGSWZMBGQFBCCEZTTPOYNIVUJRVSZSCYSEYJWYHUJRVSZSCRNECPFHHZJBUHDHSNNZQKADMGFBPGBZUNVFIGNWLGCWSATVSSWWPGZHNETEBEJFBCZDPYJWOSFDVWOTANCZIHCYIMJSIGFQLYNZZSETSYSEUMHRLAAGSEFUSKBZUEJQVTDZVCFHLAAJSFJSCNFSJKCFBCFSPITQHZJLBMHECNHFHGNZIEWBLGNFMHNMHMFSVPVHSGGMBGCWSEZSZGSEPFQEIMQEZZJIOGPIOMNSSOFWSKCRLAAGSKNEAHBBSKKEVTZSSOHEUTTQYMCPHZJFHGPZQOZHLCFSVYNFYYSEZGNTVRAJVTEMPADZDSVHVYJWHGQFWKTSNYHTSZFYHMAEJMNLNGFQNFZWSKCCJHPEHZZSZGDZDSVHVYJWHGQFWKTSNYHTSZFYHMAEDNJZQAZSCHPYSKXLHMQZNKOIOKHYMKKEIKCGSGYBPHPECKCJJKNISTJJZMHTVRHQSGQMBWHTSPTHSNFQZKPRLYSZDYPEMGZILSDIOGGMNYZVSNHTAYGFBZZYJKQELSJXHGCJLSDTLNEHLYZHVRCJHZTYWAFGSHBZDTNRSESZVNJIVWFIVYSEJHFSLSHTLNQEIKQEASQJVYSEVYSEUYSMBWNSVYXEIKWYSYSEYKPESKNCGRHGSEZLNGHTSIZHSZZHCUJWARNEHZZIWHZDZMADNGPNSYFZUWZSLXJFBCGEANWHSYSEGGNIVPFLUGCEUWTENKCJNVTDPNXEIKWYSYSFHESFPAJSWGTYVSJIOKHRSKPEZMADLSDIVKKWSFHZBGEEATJLBOTDPMCPHHVZNYVZBGZSCHCEZZTWOOJMBYJSCYFRLSZSCYSEVYSEUNHZVHRFBCCZZYSEUGZDCGZDGMHDYNAFNZHTUGJJOEZBLYZDHYSHSGJMWZHWAFTIAAY";
    String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    Map<Character, Double> ENGLISH_LETTERS_FREQUENCY = Map.ofEntries(
            Map.entry('a', 0.082),
            Map.entry('b', 0.015),
            Map.entry('c', 0.028),
            Map.entry('d', 0.043),
            Map.entry('e', 0.13),
            Map.entry('f', 0.022),
            Map.entry('g', 0.02),
            Map.entry('h', 0.061),
            Map.entry('i', 0.07),
            Map.entry('j', 0.0015),
            Map.entry('k', 0.0077),
            Map.entry('l', 0.04),
            Map.entry('m', 0.024),
            Map.entry('n', 0.067),
            Map.entry('o', 0.075),
            Map.entry('p', 0.019),
            Map.entry('q', 0.00095),
            Map.entry('r', 0.06),
            Map.entry('s', 0.063),
            Map.entry('t', 0.091),
            Map.entry('u', 0.028),
            Map.entry('v', 0.0098),
            Map.entry('w', 0.024),
            Map.entry('x', 0.0015),
            Map.entry('y', 0.02),
            Map.entry('z', 0.00074),
            Map.entry('A', 0d),
            Map.entry('B', 1d),
            Map.entry('C', 2d),
            Map.entry('D', 3d),
            Map.entry('E', 4d),
            Map.entry('F', 5d),
            Map.entry('G', 6d),
            Map.entry('H', 7d),
            Map.entry('I', 8d),
            Map.entry('J', 9d),
            Map.entry('K', 10d),
            Map.entry('L', 11d),
            Map.entry('M', 12d),
            Map.entry('N', 13d),
            Map.entry('O', 14d),
            Map.entry('P', 15d),
            Map.entry('Q', 16d),
            Map.entry('R', 17d),
            Map.entry('S', 18d),
            Map.entry('T', 19d),
            Map.entry('U', 20d),
            Map.entry('V', 21d),
            Map.entry('W', 22d),
            Map.entry('X', 23d),
            Map.entry('Y', 24d),
            Map.entry('Z', 25d),
            Map.entry(' ', 22d),
            Map.entry('.', 23d),
            Map.entry(',', 24d),
            Map.entry('\n', 25d));

    Map<String, Integer> BASE_64_CHART = Map.ofEntries(
            Map.entry("A", 0),
            Map.entry("B", 1),
            Map.entry("C", 2),
            Map.entry("D", 3),
            Map.entry("E", 4),
            Map.entry("F", 5),
            Map.entry("G", 6),
            Map.entry("H", 7),
            Map.entry("I", 8),
            Map.entry("J", 9),
            Map.entry("K", 10),
            Map.entry("L", 11),
            Map.entry("M", 12),
            Map.entry("N", 13),
            Map.entry("O", 14),
            Map.entry("P", 15),
            Map.entry("Q", 16),
            Map.entry("R", 17),
            Map.entry("S", 18),
            Map.entry("T", 19),
            Map.entry("U", 20),
            Map.entry("V", 21),
            Map.entry("W", 22),
            Map.entry("X", 23),
            Map.entry("Y", 24),
            Map.entry("Z", 25),
            Map.entry("a", 26),
            Map.entry("b", 27),
            Map.entry("c", 28),
            Map.entry("d", 29),
            Map.entry("e", 30),
            Map.entry("f", 31),
            Map.entry("g", 32),
            Map.entry("h", 33),
            Map.entry("i", 34),
            Map.entry("j", 35),
            Map.entry("k", 36),
            Map.entry("l", 37),
            Map.entry("m", 38),
            Map.entry("n", 39),
            Map.entry("o", 40),
            Map.entry("p", 41),
            Map.entry("q", 42),
            Map.entry("r", 43),
            Map.entry("s", 44),
            Map.entry("t", 45),
            Map.entry("u", 46),
            Map.entry("v", 47),
            Map.entry("w", 48),
            Map.entry("x", 49),
            Map.entry("y", 50),
            Map.entry("z", 51),
            Map.entry("0", 52),
            Map.entry("1", 53),
            Map.entry("2", 54),
            Map.entry("3", 55),
            Map.entry("4", 56),
            Map.entry("5", 57),
            Map.entry("6", 58),
            Map.entry("7", 59),
            Map.entry("8", 60),
            Map.entry("9", 61),
            Map.entry("+", 62),
            Map.entry("/", 63),
            Map.entry("=", 64)
    );

    Map<Character, Double> SUBTASK3_ALPHABET = Map.ofEntries(
            Map.entry('A', 0.082),
            Map.entry('B', 0.015),
            Map.entry('C', 0.028),
            Map.entry('D', 0.043),
            Map.entry('E', 0.13),
            Map.entry('F', 0.022),
            Map.entry('G', 0.02),
            Map.entry('H', 0.061),
            Map.entry('I', 0.07),
            Map.entry('J', 0.0015),
            Map.entry('K', 0.0077),
            Map.entry('L', 0.04),
            Map.entry('M', 0.024),
            Map.entry('N', 0.067),
            Map.entry('O', 0.075),
            Map.entry('P', 0.019),
            Map.entry('Q', 0.00095),
            Map.entry('R', 0.06),
            Map.entry('S', 0.063),
            Map.entry('T', 0.091),
            Map.entry('U', 0.028),
            Map.entry('V', 0.0098),
            Map.entry('W', 0.024),
            Map.entry('X', 0.0015),
            Map.entry('Y', 0.02),
            Map.entry('Z', 0.00074));

}