package mx.pagando.check.hardware;

interface SelectedAppCallback{
    void cancel();
    void selectApp(int which);
}