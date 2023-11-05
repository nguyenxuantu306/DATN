package com.greenfarm;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@SpringBootApplication
public class GreenfarmApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(GreenfarmApplication.class, args);
	}

	@Bean
	public Cloudinary cloudinary() {
		Cloudinary c = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", "doubw3miw",
				"api_key", "273892997856183",
				"api_secret", "RQMrIJtNw5RlHOnJSrWskLUJr6M",
				"secure", true
				));
		return c;
	}

	

}
