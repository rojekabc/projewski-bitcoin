package pl.projewski.cryptogames.gambler;

public enum ELooseTechnique {
    /* double bet */
    DOUBLE,
    /* double bet plus one with zero points */
    ZERO2N1,
    /* double bet plus one */
    ALL2N1,
    /* bet the base always after loose */
    BASE,
    /* No double - try triple */
    TRIO,
    /* No double - try triple */
    ZEROTRIO,
    ARRAY,
    /* double bet plus one with zero points and swapping on base of statistic */
    ZERO2N1SWAP;
}
