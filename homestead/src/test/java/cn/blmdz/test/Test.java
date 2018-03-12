package cn.blmdz.test;

public class Test<T> {

	public void t(Class<T> clazz) {
		System.out.println(clazz.getSimpleName());
	}
	public void t(T t) {
		System.out.println(t.getClass().getSimpleName());
	}
	public static void main(String[] args) {
		new Test<B>().t(new B());
		new Test<B>().t(B.class);
	}
}
class B {
	
}
