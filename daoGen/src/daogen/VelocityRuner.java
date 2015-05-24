package daogen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public abstract class VelocityRuner {

	static {
		try {
			Velocity.init();
		} catch (Exception e) {
			throw new RuntimeException("velocity init error.", e);
		}
	}

	public static void run(Map<String, Object> context, String tpl,
			String output) throws Exception {

		VelocityContext ctx = new VelocityContext();

		Iterator<Entry<String, Object>> it = context.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> en = it.next();
			ctx.put(en.getKey(), en.getValue());
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(output), "UTF-8");
		InputStreamReader reader = new InputStreamReader(new FileInputStream(tpl));
		Velocity.evaluate(ctx, writer, "run", reader);

		writer.flush();
		writer.close();
		
		reader.close();
	}
	public static String run(Map<String, Object> context, String tpl) throws Exception {

		VelocityContext ctx = new VelocityContext();

		Iterator<Entry<String, Object>> it = context.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> en = it.next();
			ctx.put(en.getKey(), en.getValue());
		}
		StringWriter writer = new StringWriter();
		InputStreamReader reader = new InputStreamReader(new FileInputStream(tpl));
		Velocity.evaluate(ctx, writer, "run", reader);

		writer.flush();
		writer.close();
		
		reader.close();
		return writer.toString();
	}
}
