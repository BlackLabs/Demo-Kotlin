package mx.pagando.check.services;

interface SelectAppCallback {
    void cancel();
    void selectApp(int which);
}