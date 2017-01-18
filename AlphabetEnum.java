/**
 * Created by tyler on 1/18/2017.
 */

public enum AlphabetEnum {
    A (0,'a'),
    B (1,'b'),
    C (2,'c'),
    D (3,'d'),
    E (4,'e'),
    F (5,'f'),
    G (6,'g'),
    H (7,'h'),
    I (8,'i'),
    J (9,'j'),
    K (10,'k'),
    L (11,'l'),
    M (12,'m'),
    N (13,'n'),
    O (14,'o'),
    P (15,'p'),
    Q (16,'q'),
    R (17,'r'),
    S (18,'s'),
    T (19,'t'),
    U (20,'u'),
    V (21,'v'),
    W (22,'w'),
    X (23,'x'),
    Y (24,'y'),
    Z (25,'z');

    private int indexer;
    private char value;

    AlphabetEnum(int indexer,char value) {
        this.indexer = indexer;
        this.value = value;
    }
    //returns the index of current Enum
    public int getIndexer(){return (int)this.indexer;}
    //gets the indexer number of some char c - 'a'
    public int getIndexer(char c){return (c - A.getValue());}
    //returns the char value of an Enum
    public char getValue(){return this.value;}

}
