package table;

public class Table {
    public static final int STATE_EMPTY=0;
    public static final int STATE_WAITING=1;
    public static final int STATE_GAMEING=2;
    /**
     * Table的编号
     */
    private int num;
    /**
     * Table的状态
     */
    private int state=Table.STATE_EMPTY;

    public int getState() {
        return state;
    }
    public int getNum() {
        return num;
    }

    public void setState(int state) {
        this.state = state;
    }
    public void setNum(int num) {
        this.num = num;
    }
}
