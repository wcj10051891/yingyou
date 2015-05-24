package test;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class RequestDto {
	@Protobuf(description="牛逼", fieldType=FieldType.INT32, order=5, required=true)
	public int sn;
	@Protobuf
	public String name;
	@Protobuf
	private RequestDto r2;
	@Protobuf
	private ResponseDto r3;
	@Protobuf
	private List<ResponseDto> r4;
	
	public RequestDto() {
		// TODO Auto-generated constructor stub
	}
	
	

	public RequestDto(int sn, String name, RequestDto r2, ResponseDto r3) {
		super();
		this.sn = sn;
		this.name = name;
		this.r2 = r2;
		this.r3 = r3;
	}



	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RequestDto getR2() {
		return r2;
	}

	public void setR2(RequestDto r2) {
		this.r2 = r2;
	}

	public ResponseDto getR3() {
		return r3;
	}

	public void setR3(ResponseDto r3) {
		this.r3 = r3;
	}



	public List<ResponseDto> getR4() {
		return r4;
	}



	public void setR4(List<ResponseDto> r4) {
		this.r4 = r4;
	}
}
