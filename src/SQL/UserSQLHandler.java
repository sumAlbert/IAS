package SQL;

public class UserSQLHandler implements SQLHandler {
    private BaseConnection baseConnection;

    public UserSQLHandler(BaseConnection baseConnection){
        this.baseConnection=baseConnection;
    }

    @Override
    public boolean insertSQL(Object object) {
        return false;
    }

    @Override
    public boolean deleteSQL(Object object) {
        return false;
    }

    @Override
    public boolean updateSQL(Object object) {
        return false;
    }
}
