package test;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class AngleTest {

	public static void main(String[] args) {
//		int angle = 30;
//		Point source = new Point(3, 3);
//		Point target = new Point(6, 2);
//		double a = Math.atan2(target.getY() - source.getY(), target.getX() - source.getX());
//		if(a < 0) {
//			System.out.println(a + 2 * Math.PI);
//		}
//		double half = Math.toRadians(angle) / 2;
//		double startAngle = a - half;
//		double endAngle = a + half;
//		System.out.println("中央弧度:" + a);
//		System.out.println("start弧度:" + startAngle);
//		System.out.println("end弧度:" + endAngle);
//		System.out.println("中央角度:" + Math.toDegrees(a));
//		System.out.println("start角度:" + Math.toDegrees(startAngle));
//		System.out.println("end角度:" + Math.toDegrees(endAngle));
//		
//
//		Point monster = new Point(1, 2);
//		double a2 = Math.atan2(monster.getY() - source.getY(), monster.getX() - source.getX());
//		double pi2 = 2 * Math.PI;
//		if((a2 > startAngle && a2 < endAngle) ||
//				((a2 - pi2) > startAngle && (a2 - pi2) < endAngle) ||
//				((a2 + pi2) > startAngle && (a2 + pi2) < endAngle)) {
//			System.out.println("目标角度：" + Math.toDegrees(a2));			
//			System.out.println("in range");
//		}
//		long a = System.currentTimeMillis() / 1000;
//		double tan30 = Math.tan(Math.PI / 6);
//		System.out.println(a);
//		System.out.println(tan30);
//		double b = tan30 * a;
//		System.out.println(b);
		
		long start = System.currentTimeMillis();
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		System.out.println(bean.getName());
		long during = System.currentTimeMillis() - start;
		System.out.println(during);
	}

}
