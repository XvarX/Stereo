import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.*;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
class LabModel {
	private double L = 0;
	private double a = 0;
	private double b = 0;
	public LabModel(){
		L = 0;
		a = 0;
		b = 0;
	}
	public void setLab(double L_, double a_, double b_) {
		this.L = L_;
		this.a = a_;
		this.b = b_;
	}
}
public class Stero {
	public static void main(String args[]) throws IOException
	{
		JFrame frame = new JFrame();
		label = new JLabel();
		frame.setSize(1400,800);
		frame.add(label);
		File file = new File("C:\\Users\\Xvar\\Desktop\\ALL-2views");
		String [] fileName = file.list();
		/*for (int i = 0; i < fileName.length;i++) {
			patchporcess(fileName[i]);
		}*/
		for (int i = 0;i < fileName.length;i++) {
		    saw_image(fileName[i]);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public static BufferedImage greytoImage(int grey[][]) {
		int width = grey.length;
		int height = grey[0].length;
		BufferedImage bufferedImage1 = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		for (int i = 0;i < width;i++) {
			for(int j = 0;j < height;j++) {
				int rgb=((grey[i][j]*256)+grey[i][j])*256+grey[i][j];
				bufferedImage1.setRGB(i, j, rgb);
			}
		}
		return bufferedImage1;
	}
	public static void patchporcess(String floder) throws IOException {
		File file1 = new File("C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\view1.png");
		BufferedImage bufferedImage1 = ImageIO.read(file1);
		File file2 = new File("C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\view5.png");
		BufferedImage bufferedImage2 = ImageIO.read(file2);
		int width = bufferedImage1.getWidth();
		int height = bufferedImage1.getHeight();
		int[][]grey1 = greyValue(bufferedImage1);
		int[][]grey2 = greyValue(bufferedImage2);
		int[][]ssdleftimage = new int[width][height];
		int[][]ssdrightimage = new int[width][height];
		int[][]nccleftimage = new int[width][height];
		int[][]nccrightimage = new int[width][height];
		for (int i = 0;i < width;i++) {
			for (int j = 0; j < height;j++) {
				grey2[i][j] += 10;
			}
		}
		for (int i = 0;i < width;i++) {
			for (int j = 0; j < height; j++) {
				int distance_left_min = 0xfffffff;
				int distance_right_min = 0xfffffff;
				double ncc_left_max = 0;
				double ncc_right_max = 0;
				int leftssd = 0;
				int rightssd = 0;
				int leftncc = 0;
				int rightncc = 0;
				for (int d = 0; d<80; d++) {
					int righti = i-d;
					int rightj = j;
					int lefti = i+d;
					int leftj = j;
					int sumleft = 0;
					int sumright = 0;
					
					double leftnccfenzi = 0;
					double leftnccFL = 0;
					double leftnccFR = 0;
					double leftnccresult = 0;
					
					
					double rightnccfenzi = 0;
					double rightnccFL = 0;
					double rightnccFR = 0;
					double rightnccresult = 0;
					for (int turni = -2; turni <= 2; turni++) {
						for (int turnj = -2;turnj <= 2;turnj++) {
							int temp_i = i+turni;
							int temp_j = j+turnj;
							int temp_right_i = righti+turni;
							int temp_right_j = rightj+turnj;
							int temp_left_i = lefti+turni;
							int temp_left_j = leftj+turnj;
							if (temp_i < 0) {
								temp_i = 0;
							}
							if (temp_i >= width) {
								temp_i = width-1;
							}
							if (temp_j < 0) {
								temp_j = 0;
							}
							if (temp_j >= height) {
								temp_j = height-1;
							}
							if (temp_right_i < 0) {
								temp_right_i = 0;
							}
							if (temp_right_i >= width) {
								temp_right_i = width-1;
							}
							if (temp_right_j < 0) {
								temp_right_j = 0;
							}
							if (temp_right_j >= height) {
								temp_right_j = height-1;
							}
							if (temp_left_i < 0) {
								temp_left_i = 0;
							}
							if (temp_left_i >= width) {
								temp_left_i = width-1;
							}
							if (temp_left_j < 0) {
								temp_left_j = 0;
							}
							if (temp_left_j >= height) {
								temp_left_j = height-1;
							}
							sumleft += Math.pow(grey1[temp_i][temp_j] - grey2[temp_right_i][temp_right_j],2);
							
							
							sumright += Math.pow(grey2[temp_i][temp_j] - grey1[temp_left_i][temp_left_j],2);
							
							
							leftnccfenzi += grey1[temp_i][temp_j]*grey2[temp_right_i][temp_right_j];
							leftnccFL += grey1[temp_i][temp_j]*grey1[temp_i][temp_j];
							leftnccFR += grey2[temp_right_i][temp_right_j]*grey2[temp_right_i][temp_right_j];
							leftnccresult = leftnccfenzi/(Math.sqrt(leftnccFL)*Math.sqrt(leftnccFR));
							
							rightnccfenzi += grey2[temp_i][temp_j]*grey1[temp_left_i][temp_left_j];
							rightnccFL += grey2[temp_i][temp_j]*grey2[temp_i][temp_j];
							rightnccFR += grey1[temp_left_i][temp_left_j]*grey1[temp_left_i][temp_left_j];
							rightnccresult = rightnccfenzi/(Math.sqrt(rightnccFL)*Math.sqrt(rightnccFR));
						}
					}
					if (sumleft < distance_left_min) {
						distance_left_min = sumleft;
						leftssd = d;
					}
					if (sumright < distance_right_min) {
						distance_right_min = sumright;
						rightssd = d;
					}
					if (leftnccresult > ncc_left_max) {
						ncc_left_max = leftnccresult;
						leftncc = d;
					}
					if (rightnccresult > ncc_right_max) {
						ncc_right_max = rightnccresult;
						rightncc = d;
					}
				}
				ssdleftimage[i][j] = leftssd*3;
				ssdrightimage[i][j] = rightssd*3;
				nccleftimage[i][j] = leftncc*3;
				nccrightimage[i][j] = rightncc*3;
			}
		}
		BufferedImage bufferedSSDleft = greytoImage(ssdleftimage);
		BufferedImage bufferedSSDright= greytoImage(ssdrightimage);
		BufferedImage bufferedNCCleft= greytoImage(nccleftimage);
		BufferedImage bufferedNCCright= greytoImage(nccrightimage);
		ImageIO.write(bufferedSSDleft, "png", new File(
				"C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\"+floder+"_disp1_SSD_10.png"));
		ImageIO.write(bufferedSSDright, "png", new File(
				"C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\"+floder+"_disp5_SSD_10.png"));
		ImageIO.write(bufferedNCCleft, "png", new File(
				"C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\"+floder+"_disp1_NCC_10.png"));
		ImageIO.write(bufferedNCCright, "png", new File(
				"C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\"+floder+"_disp5_NCC_10.png"));
	}
	public static double[][][] rgbtolab(BufferedImage bufferedImage1) {
		int width = bufferedImage1.getWidth();
		int height = bufferedImage1.getHeight();
		double lab[][][] = new double[width][height][3];
		double rgbcell[][][] = new double[width][height][3];
		int result[][] = new int[width][height];
		for (int i = 0;i < width;i++) {
			for (int j = 0;j < height;j++) {
				int rgb = bufferedImage1.getRGB(i, j);
				double r  =(rgb & 0xff0000) >> 16;
			    double g = (rgb & 0xff00) >> 8;
	            double b = rgb & 0xff;
	            rgbcell[i][j][0] = r;
	            rgbcell[i][j][1] = g;
	            rgbcell[i][j][2] = b;
	            double B=gamma(b/255.0f);
	            double G=gamma(g/255.0f);
	            double R=gamma(r/255.0f);
	            double X=0.412453*R+0.357580*G+0.180423*B;
	            double Y=0.212671*R+0.715160*G+0.072169*B;
	            double Z=0.019334*R+0.119193*G+0.950227*B;
	            X/=95.047;
	            Y/=100.0;
	            Z/=108.883;
	            double FX = X > 0.008856f ? Math.pow(X,1.0f/3.0f) : (7.787f * X +0.137931f);
	            double FY = Y > 0.008856f ? Math.pow(Y,1.0f/3.0f) : (7.787f * Y +0.137931f);
	            double FZ = Z > 0.008856f ? Math.pow(Z,1.0f/3.0f) : (7.787f * Z +0.137931f);
	            double L = Y > 0.008856f ? (116.0f * FY - 16.0f) : (903.3f * Y);
	            double a_ = 500.f * (FX - FY);
	            double b_ = 200.f * (FY - FZ);
	            lab[i][j][0] = L;
	            lab[i][j][1] = a_;
	            lab[i][j][2] = b_;
	            //stero
			}
		}
		return lab;
	}
	
	
	public static double[][][] myGetRgb(BufferedImage bufferedImage1){
		int width = bufferedImage1.getWidth();
		int height = bufferedImage1.getHeight();
		double rgbcell[][][] = new double[width][height][3];
		for (int i = 0;i < width;i++) {
			for (int j = 0;j < height;j++) {
				int rgb = bufferedImage1.getRGB(i, j);
				double r  =(rgb & 0xff0000) >> 16;
			    double g = (rgb & 0xff00) >> 8;
	            double b = rgb & 0xff;
	            rgbcell[i][j][0] = r;
	            rgbcell[i][j][1] = g;
	            rgbcell[i][j][2] = b;
			}
		}
		return rgbcell;
	}
	public static double omega(double[] p, double[] q, int px, int py, int qx, int qy) {
		double rp = 7.0;
		double rc = 7.0;
		double k = 1.0;
		double distance_L = p[0] - q[0];
		double distance_a = p[1] - q[1];
		double distance_b = p[2] - q[2];
		double detacolor = Math.sqrt(distance_L*distance_L+distance_a*distance_a
                +distance_b*distance_b);
		detacolor /= rp;
		double detadistance = Math.sqrt((px-qx)*(px-qx) + (py-qy)*(py-qy));
		detadistance /= rc;
		double result = k*Math.exp(-(detacolor+detadistance));
		return result;
	}
	public static void saw_image(String floder) throws IOException {
		File file1 = new File("C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\view1.png");
		BufferedImage bufferedImage1 = ImageIO.read(file1);
		File file2 = new File("C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\view5.png");
		BufferedImage bufferedImage2 = ImageIO.read(file2);
		int width = bufferedImage1.getWidth();
		int height = bufferedImage1.getHeight();
		double lableft[][][] = new double[width][height][3];
		double rgbleftcell[][][] = new double[width][height][3];
		double labright[][][] = new double[width][height][3];
		double rgbrightcell[][][] = new double[width][height][3];
		int result[][] = new int[width][height];
		int resultright[][] = new int[width][height];
		lableft = rgbtolab(bufferedImage1);
		labright = rgbtolab(bufferedImage2);
		rgbleftcell = myGetRgb(bufferedImage1);
		rgbrightcell = myGetRgb(bufferedImage2);
		for(int i = 0;i < width;i++) {
			for(int j = 0;j < height;j++) {
				int pd = 0;
				int pdright = 0;
				double mini = 0xfffffff;
				double miniright = 0xfffffff;
				for (int d = 0;d <= 79;d++ ) {
					int righti = i-d;
					int rightj = j;
					
					int lefti = i+d;
					int leftj = j;
					
					
					int right_i = righti;
					if (right_i < 0) {
						right_i = 0;
					}
					if (right_i >= width) {
						right_i = width-1;
					}
					
					int left_i = lefti;
					if (left_i < 0) {
						left_i = 0;
					}
					if (left_i >= width) {
						left_i = width-1;
					}
					double sumfenmu = 0;
					double sumfenzi = 0;
					
					double sumfenmuright = 0;
					double sumfenziright = 0;
					for (int turni = -2; turni <= 2; turni++) {
						for (int turnj = -2;turnj <= 2;turnj++) {
							int temp_i = i+turni;
							int temp_j = j+turnj;
							int temp_right_i = righti+turni;
							int temp_right_j = rightj+turnj;
							int temp_left_i = lefti+turni;
							int temp_left_j = leftj+turnj;
							if (temp_i < 0) {
								temp_i = 0;
							}
							if (temp_i >= width) {
								temp_i = width-1;
							}
							if (temp_j < 0) {
								temp_j = 0;
							}
							if (temp_j >= height) {
								temp_j = height-1;
							}
							if (temp_right_i < 0) {
								temp_right_i = 0;
							}
							if (temp_right_i >= width) {
								temp_right_i = width-1;
							}
							if (temp_right_j < 0) {
								temp_right_j = 0;
							}
							if (temp_right_j >= height) {
								temp_right_j = height-1;
							}
							if (temp_left_i < 0) {
								temp_left_i = 0;
							}
							if (temp_left_i >= width) {
								temp_left_i = width-1;
							}
							if (temp_left_j < 0) {
								temp_left_j = 0;
							}
							if (temp_left_j >= height) {
								temp_left_j = height-1;
							}
							double omegapq = omega(lableft[i][j],lableft[temp_i][temp_j], i,j,i+turni,j+turnj);
							double omegapqd = omega(labright[right_i][rightj],labright[temp_right_i][temp_right_j], 
									                righti,rightj,righti+turni, rightj+turnj);
							double rgbdis = 0;
							for(int rgbi = 0;rgbi < 3;rgbi++) {
								rgbdis += Math.abs(rgbleftcell[temp_i][temp_j][rgbi] - rgbrightcell[temp_right_i][temp_right_j][rgbi]);
							}
							sumfenmu+=omegapq*omegapqd*rgbdis;
							sumfenzi += omegapq*omegapqd;
							
							
							double omegapqright = omega(labright[i][j],labright[temp_i][temp_j], i,j,i+turni,j+turnj);
							double omegapqdright = omega(lableft[left_i][leftj],lableft[temp_left_i][temp_left_j], 
									                lefti,leftj,lefti+turni, leftj+turnj);
							rgbdis = 0;
							for(int rgbi = 0;rgbi < 3;rgbi++) {
								rgbdis += Math.abs(rgbrightcell[temp_i][temp_j][rgbi] - rgbleftcell[temp_left_i][temp_left_j][rgbi]);
							}
							sumfenmuright+=omegapqright*omegapqdright*rgbdis;
							sumfenziright += omegapqright*omegapqdright;
						}
					}
					if (sumfenmu/sumfenzi < mini) {
						mini = sumfenmu/sumfenzi;
						pd = d;
					}
					
					if (sumfenmuright/sumfenziright < miniright) {
						miniright = sumfenmuright/sumfenziright;
						pdright = d;
					}
				}
				result[i][j] = pd*3;
				resultright[i][j] = pdright*3;
			}
		}
		BufferedImage bufferedASWleft = greytoImage(result);
		BufferedImage bufferedASWright = greytoImage(resultright);
		imageshow(bufferedASWright);
		ImageIO.write(bufferedASWleft, "png", new File(
				"C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\"+floder+"_disp1_ASW.png"));
		ImageIO.write(bufferedASWright, "png", new File(
				"C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\"+floder+"_disp5_ASW.png"));
	}
	public static double fly(double t) {
		double result;
		if (t > Math.pow(6.0/29,3)) {
			result = Math.pow(t, 1.0/3);
		} else {
			result = (1.0/3)*(29.0/6)*(29.0/6)*t + 4.0/29;
		}
		return result;
	}
	public static double gamma(double x) {
		return x>0.04045?Math.pow((x+0.055f)/1.055f,2.4f):x/12.92;
	}
	public static void imageshow(BufferedImage img_buff) {
		ImageIcon newicon = new ImageIcon(img_buff);
		label.setIcon(newicon);
	}
	public static int[][]greyValue(BufferedImage img_buff) {
		BufferedImage bufferedImage = img_buff;
		int ImageWidth = img_buff.getWidth();
		int ImageHeight = img_buff.getHeight();
		int greyValue[][] = new int[ImageWidth][ImageHeight];
		int i = 0;
		int j = 0;
		while (i < ImageWidth) {
			j = 0;
			while (j < ImageHeight) {
				int a = bufferedImage.getRGB(i,j);
			    int r  =(a & 0xff0000) >> 16;
		        int g = (a & 0xff00) >> 8;
		        int b = a & 0xff;
			    int grey = (int) (r * 0.3 + g * 0.59 + b * 0.11);
			    greyValue[i][j] = grey;
			    j++;
			}
			i++;
		}
		return greyValue;
	}
	public static double[][][] disparity(int grey1[][], int grey2[][]) {
		int width = grey1.length;
		int height = grey1[0].length;
		double[][][] disp = new double[width][height][80];
		for(int i = 0; i < width;i++) {
			for(int j = 0;j < height;j++) {
				for (int d = 0;d <= 79; d++) {
					int sum = 0;
					int righti = i-d;
					int rightj = j;
					for(int turni = -1; turni <= 1;turni++) {
						for(int turnj = -1;turnj <= 1;turnj++) {
							int temp_i = i+turni;
							int temp_j = j+turnj;
							int temp_right_i = righti+turni;
							int temp_right_j = rightj+turnj;
							if (temp_i < 0) {
								temp_i = 0;
							}
							if (temp_i >= width) {
								temp_i = width-1;
							}
							if (temp_j < 0) {
								temp_j = 0;
							}
							if (temp_j >= height) {
								temp_j = height-1;
							}
							if (temp_right_i < 0) {
								temp_right_i = 0;
							}
							if (temp_right_i >= width) {
								temp_right_i = width-1;
							}
							if (temp_right_j < 0) {
								temp_right_j = 0;
							}
							if (temp_right_j >= height) {
								temp_right_j = height-1;
							}
							sum+=Math.abs(grey1[temp_i][temp_j] - grey2[temp_right_i][temp_right_j]);
						}
					}
					disp[i][j][d] = sum;
				}
			}
		}
		return disp;
	}
	public static void SGM(String floder) throws IOException {
		File file1 = new File("C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\view1.png");
		BufferedImage bufferedImage1 = ImageIO.read(file1);
		File file2 = new File("C:\\Users\\Xvar\\Desktop\\ALL-2views\\"+floder+"\\view5.png");
		BufferedImage bufferedImage2 = ImageIO.read(file2);
		BufferedImage bufferedResult = ImageIO.read(file1);
		int width = bufferedImage1.getWidth();
		int height = bufferedImage1.getHeight();
		globalwidth = width;
		globalheight = height;
		int[][]grey1 = greyValue(bufferedImage1);
		leftgrey = grey1;
		int[][]grey2 = greyValue(bufferedImage2);
		rightgrey = grey2;
		cost = disparity(grey1,grey2);
		
	}
	public static double Lleft(int x,int y,int d) {
		if (x == 0) {
			return cost[x][y][d];
		} else {
			int P1 = 10;
			int P2 = 20;
			int dminus = d-1;
			int dpuls = d+1;
			if (dminus < 0) {
				dminus = 0;
			}
			if (dpuls >= globalwidth) {
				dpuls = globalwidth;
			}
			double temp1 = Lleft(x-1,y,d);
			double temp2 = Lleft(x-1,y,dminus);
			double temp3 = Lleft(x-1,y,dpuls);
			double temp4 = 0xfffffff;
			for (int i = 0; i < 80; i++) {
				if (i != d&&i != d-1&&i != d+1) {
					if (temp4 > Lleft(x-1,y,i)) {
						temp4 =  Lleft(x-1,y,i);
					}
				}
			}
			temp2 += P1;
			temp3 += P1;
			temp4 += P2;
			double minius = temp1;
			if (minius > temp2) {
				minius = temp2;
			}
			if (minius > temp3) {
				minius = temp3;
			}
			if (minius > temp4) {
				minius = temp4;
			}
			double temp5 = 0xffffffff;
			for (int i = 0; i < 80; i++) {
				if (i != d) {
					if (temp5 > Lleft(x-1,y,i)) {
						temp5 =  Lleft(x-1,y,i);
					}
				}
			}
			
			
			int Il = leftgrey[x][y];
			int Ilm;
			int Ilp;
			if(x-1 < 0) {
				Ilm = leftgrey[x][y];
			} else {
				Ilm = leftgrey[x-1][y];
			}
			if(x+1 >= globalwidth) {
				Ilp = leftgrey[x][y];
			} else {
				Ilp = leftgrey[x+1][y];
			}
			
			
			int Ir = rightgrey[x][y];
			int Irm;
			int Irp;
			if(x-1 < 0) {
				Irm = rightgrey[x][y];
			} else {
				Irm = rightgrey[x-1][y];
			}
			if(x+1 >= globalwidth) {
				Irp = rightgrey[x][y];
			} else {
				Irp = rightgrey[x+1][y];
			}
			//to be continue
		}
		return d;
	}
	private static int globalwidth;
	private static int globalheight;
	private static double[][][] cost;
	private static int[][] leftgrey;
	private static int[][] rightgrey;
	private static JLabel label;
}