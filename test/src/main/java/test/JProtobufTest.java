package test;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

public class JProtobufTest {

	public static void main(String[] args) throws Exception {
		Codec<RequestDto> c = ProtobufProxy.create(RequestDto.class);
		RequestDto r = new RequestDto();
		r.setSn(1);
		r.setName("name2");
		r.setR3(new ResponseDto(3));
		r.setR2(new RequestDto(4, "5", new RequestDto(6, "7", null, new ResponseDto(8)), new ResponseDto(9)));
		byte[] b = c.encode(r);
		RequestDto r2 = c.decode(b);
		System.out.println(r2.getSn());
		System.out.println(r2.getName());
		System.out.println(r2.getR2().getName());
		System.out.println(r2.getR2().getSn());
		System.out.println(r2.getR2().getR2());
		System.out.println(r2.getR2().getR3().getA());
		
		String idl = ProtobufIDLGenerator.getIDL(RequestDto.class);
		System.out.println("神器：" + idl);
	}

}
