package com.greenfarm.dto;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.WebcamDevice;

public class WebcamDTO implements WebcamDevice{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension[] getResolutions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getResolution() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResolution(Dimension size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

}
