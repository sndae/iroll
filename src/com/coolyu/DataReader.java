
package com.coolyu;

import java.io.*;
import java.util.*;

public class DataReader {	
	public String str;
	/* Pass Activity.getResources().openRawResource(R.raw.id) as isr */
	public void readMapData(InputStream is, GameData data) {
		data.door1 = new ArrayList<PointInt>();
		data.door2 = new ArrayList<PointInt>();
		BufferedReader fin = new BufferedReader(new InputStreamReader(is));
        try {
            /* Get the # of rows, columns, doors of the map */
        	str = fin.readLine();
        	int row = Integer.parseInt(str);
        	data.row = row;
        	str = fin.readLine();
        	int col = Integer.parseInt(str);
        	data.col = col;
        	str = fin.readLine();
        	data.doorNum = Integer.parseInt(str);
        	
        	/* Record the 2D map array.
        	 * Also record the doors' pixels in ArrayLists door1 & door2 */
        	data.map = new char[row][col];
        	for (int i = 0; i < row; i++) {
    			str = fin.readLine();
        		for (int j = 0; j < col; j++) {
        			char value = (char) ((int) str.charAt(j) - '0');
        			data.map[i][j] = value;
        			
        			/* Detect for doors */
        			if (value == (char)2) {
        				data.door1.add(new PointInt(j, i));
        			}
        			else if (value == (char)3) {
        				data.door2.add(new PointInt(j, i));
        			}
        		}
        	}
        	fin.close();    
   	
        } catch (Exception e) {}

	}
	
	public void readBallData(InputStream is, GameData data) {
		BufferedReader fin = new BufferedReader(new InputStreamReader(is));
		try {
			/* Get ballYStart, ballYEnd */
        	str = fin.readLine();
        	int yStart = Integer.parseInt(str);
        	data.ballYStart = yStart;
        	
        	str = fin.readLine();
        	int yEnd = Integer.parseInt(str);
        	data.ballYEnd = yEnd;
        	
        	/* Get Pairs ballX[] ("for each" strangely failed)*/
			int N = yEnd - yStart + 1;
			data.ballX = new Pair[N];
			for (int i = 0; i < N; i++) {
				data.ballX[i] = new Pair();
				str = fin.readLine();
				/* Two numbers in one line */
				StringTokenizer st = new StringTokenizer(str);
				data.ballX[i].start = Integer.parseInt(st.nextToken());
				data.ballX[i].end = Integer.parseInt(st.nextToken());
			}  	
			fin.close();
			
		} catch (Exception e) {}
	}
}
