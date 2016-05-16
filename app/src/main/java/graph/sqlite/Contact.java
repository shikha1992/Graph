package graph.sqlite;


public class Contact {
    //private variables
    int _id;
    String _x;
    String _point;
    String _y;

    // Empty constructor
    public Contact() {

    }

    public Contact(int id, String x, String y,String point) {
        this._id = id;
        this._x = x;
        this._y = y;
        this._point = point;
    }

    // constructor
    public Contact(String x, String y,String point) {
        this._x = x;
        this._y = y;
        this._point = point;
    }

    public int getID() {
        return _id;
    }

    public void setID(int _id) {
        this._id = _id;
    }

    public String getx() {
        return _x;
    }

    public void setx(String _x) {
        this._x = _x;
    }

    public String get_point() {
        return _point;
    }

    public void set_point(String _point) {
        this._point = _point;
    }

    public String get_y() {
        return _y;
    }

    public void set_y(String _y) {
        this._y = _y;
    }


}