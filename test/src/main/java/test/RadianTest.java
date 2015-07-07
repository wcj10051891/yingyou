package test;


public class RadianTest {
	public static void main(String[] args) {
//		Point p1 = new Point(1, 1);
//		Point p2 = new Point(-1, -1);
//		double d = Math.atan2(p2.y - p1.y, p2.x - p1.x);
//		System.out.println(d);
//		System.out.println(Math.toDegrees(d));
		
		int direction = 1;
		int angle = 120;
//		log.debug("攻击者" + attacker.getId() + "坐标：" + attacker.getPosition().toString());
		//最小角度
		double minAngle = Math.PI / 4 * direction - ((double) angle) / 180 * Math.PI / 2;
		//最大角度
		double maxAngle = Math.PI / 4 * direction + ((double) angle) / 180 * Math.PI / 2;
		if (maxAngle > Math.PI * 2) {
			maxAngle = maxAngle - Math.PI * 2;
			minAngle = minAngle - Math.PI * 2;
		}
		System.out.println("扇形角度：" + Math.PI / 4 * direction);
		System.out.println("扇形角度最小：" + minAngle);
		System.out.println("扇形角度最大：" + maxAngle);
		System.out.println("扇形角度：" + Math.PI / 4 * direction);
		System.out.println("扇形角度最小：" + Math.toDegrees(minAngle));
		System.out.println("扇形角度最大：" + Math.toDegrees(maxAngle));
	}
}
