import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PizzaMain {
	
	static Boolean isCountLeft = false;
	static Boolean isCountRight = false;
    static Boolean isCountUp = false;
    static Boolean isCountDown = false;
    static Boolean isHoleDetected = false;
    static int mashroomCounter = 0;
    static int iterateLimit = 0;
    static int tomatoCounter = 0;
    static int cellReallyCount = 0;
    static int cellCounter = 0;
    static int maxCells = 0;
    static int minIng = 0;
    static int rowNum;
    static int columnNum;
    static char [][] pizza;
    static char [][] slicedPizza;
    static ArrayList<String> slices;
    
    static int iterateRowLimit = 0;
    static int iterateColumnLimit = 0;
    static Boolean isReadyToPublish = false;
    static Boolean isLineReadyToPublish = false;
    static Boolean isPossibleSlice = false;
    static ArrayList<String> sliceCoordinates = new ArrayList<>();
    static int cycleCounter = 0;

    public static void main(String[] args) throws InterruptedException {
        //String file = "example.in";
        //String file = "small.in";
        //String file = "medium.in";
        String file = "big.in";
        pizza = loadPizza(file);
        String [] restrictions = loadRestrictions(file).split(" ");
        minIng = Integer.parseInt(restrictions[0]);
        maxCells = Integer.parseInt(restrictions[1]);

        slicedPizza = new char [pizza.length][pizza[0].length];
        slices = new ArrayList<>();
        rowNum = pizza.length;
        columnNum = pizza[0].length;
        for (int i=0; i<pizza.length;i++){//iterate to slice two-dimensional slices
            for (int j=0; j<pizza[i].length; j++){
                if(slicedPizza[i][j] != '?'){
                	iterateDownInCaseOfRectangularSlice(i, j);
                    if(mashroomCounter >= minIng && tomatoCounter >= minIng && !isHoleDetected){
                    	isLineReadyToPublish = true;
                    }
                    else if(j != (columnNum-1)){
                    	iterateRectangularSlice(i, j);
                    }
                    addAsRectangularSlice(i, j);
                }
            }
        }//for calculating the statistics -> the cells which are not part of any slices should be set as original one.
        for(int i=0; i<slicedPizza.length; i++){
            for(int j=0; j<slicedPizza[i].length; j++){
                if(slicedPizza[i][j] != '?'){
                    slicedPizza[i][j] = pizza[i][j];
                }
            }
        }
        for(int i=0; i<slicedPizza.length; i++){
        	for(int j=0; j<slicedPizza[i].length; j++){
        		if(slicedPizza[i][j] != '?'){
        			cellCounter = 0;
                    mashroomCounter = 0;
                    tomatoCounter = 0;
                    isHoleDetected = false;
        			iterateToLeft(i, j);
	                iterateToRight(i, j);
	                iterateToUp(i, j);
	                iterateToDown(i, j);
	                definitionOfSlice(i, j);
        		}
        	}
        }
        generateSubmissionFile(slices);   
        System.out.println("The program finishes.");
        
        for (int i=0; i<pizza.length;i++){
            for (int j=0; j<pizza[i].length; j++){
            	if(i == 0 && j == 35){
            		for (int l=0; l<3;l++){
                        for (int k=0; k<4; k++){
                        	System.out.print(pizza[i+l][j+k]);
                        }
                        System.out.print("\n");
                    }
            	}
            }
        }
    }
    
    public static void iterateDownInCaseOfRectangularSlice(int i, int j){
    	cellCounter = 0;
        mashroomCounter = 0;
        tomatoCounter = 0;
        isHoleDetected = false;
        isReadyToPublish = false;
        isLineReadyToPublish = false;
    	
    	iterateRowLimit = rowNum-i;
    	iterateColumnLimit = columnNum-j;
        if(iterateRowLimit > maxCells){
            iterateRowLimit = maxCells;
        }
        if(iterateColumnLimit > maxCells){
        	iterateColumnLimit = maxCells;
        }
        for(int l=0; l<iterateRowLimit; l++){
            if(slicedPizza[i+l][j] != '?'){
                if(pizza[i+l][j] == 'M'){
                    mashroomCounter++;
                }
                else{
                    tomatoCounter++;
                }
            }
            else{
                isHoleDetected = true;
            }
        }
    }
    
    public static void addAsRectangularSlice(int i, int j){
    	String slice="";
        if(isReadyToPublish){
    		for(int l=0; l<iterateRowLimit; l++){
                for(int k=0; k<iterateColumnLimit; k++){
                	slicedPizza[i+l][j+k] = '?';
                	slice += ((i+l)+","+(j+k)+" ");
                }
            }
    	}
        if(isLineReadyToPublish){
        	for(int l=0; l<iterateRowLimit; l++){
        		slicedPizza[i+l][j] = '?';
        		slice += ((i+l)+","+(j)+" ");
        	}
        }
        if(!slice.equals("")){
        	slices.add(slice);
        }
    }
    
    public static void generateSubmissionFile(ArrayList<String> arrayOfSlices) {
        //result_output
        String file = "submission.out";
        String startXCoordinate;
        String startYCoordinate;
        String endXCoordinate;
        String endYCoordinate;
        String [] coordinates;
        try (PrintWriter writer = new PrintWriter(file, "UTF-8")){
            writer.println(arrayOfSlices.size());
            for(int i=0; i<arrayOfSlices.size(); i++){
                coordinates = arrayOfSlices.get(i).split(" ");
                startXCoordinate = coordinates[0].split(",")[0];
                startYCoordinate = coordinates[0].split(",")[1];
                endXCoordinate = coordinates[(coordinates.length-1)].split(",")[0];
                endYCoordinate = coordinates[(coordinates.length-1)].split(",")[1];
                
                /*int m=0;
                int t=0;
                for (int h=0; h<pizza.length;h++){
                    for (int j=0; j<pizza[h].length; j++){
                    	if(h == Integer.parseInt(startXCoordinate) && j == Integer.parseInt(startYCoordinate)){
                    		for (int l=0; l<(Integer.parseInt(endXCoordinate)-Integer.parseInt(startXCoordinate));l++){
                                for (int k=0; k<(Integer.parseInt(endYCoordinate)-Integer.parseInt(startYCoordinate)); k++){
                                	if(pizza[h+l][j+k] == 'M'){
                                		m++;
                                	}
                                	else{
                                		t++;
                                	}
                                }
                            }
                    		if(m < minIng || t < minIng){
                    			System.out.println(h+","+j);
                    		}
                    	}
                    }
                }*/
                
                writer.println(startXCoordinate+" "+startYCoordinate+" "+endXCoordinate+" "+endYCoordinate);
            }
            writer.close();
        }catch (Exception e) {
            System.out.println("ERROR");
        }
	}
    
    public static void iterateRectangularSlice(int i , int j){
    	cellCounter = 0;
        mashroomCounter = 0;
        tomatoCounter = 0;
        isHoleDetected = false;
        cycleCounter = 0;
    	while(iterateRowLimit != 1){
    		cycleCounter++;
    		if(maxCells % 2 == 0){
        		iterateRowLimit = maxCells/(2*cycleCounter);
        	}
        	else{
        		iterateRowLimit = (maxCells-1)/(2*cycleCounter);
        	}
    		if(iterateRowLimit > rowNum-i){
    			iterateRowLimit = rowNum-i;
    		}
    		iterateColumnLimit = maxCells/iterateRowLimit;
    		if(iterateColumnLimit > columnNum-j){
    			iterateColumnLimit = columnNum-j;
    		}
    		for(int l=0; l<iterateRowLimit; l++){
                for(int k=0; k<iterateColumnLimit; k++){
                	if(slicedPizza[i+l][j+k] != '?'){
                		if(pizza[i+l][j+k] == 'M'){
                			mashroomCounter++;
                        }
                        else{
                        	tomatoCounter++;
                        }
                        cellCounter++;
                        }
                    else{
                        isHoleDetected = true;
                    }
                }
            }
    		if(mashroomCounter >= minIng && tomatoCounter >= minIng && !isHoleDetected){
    			isReadyToPublish = true;
                break;
            }
    		else{
    			isReadyToPublish = false;
    		}
    	}
    }
    
    public static void iterateToDown(int i, int j){
		//iterating to down
        iterateLimit = rowNum-i;
        if(iterateLimit > maxCells){
            iterateLimit = maxCells;
        }
        for(int l=0; l<iterateLimit; l++){
            if(slicedPizza[i+l][j] != '?'){
                if(slicedPizza[i+l][j] == 'M'){
                    mashroomCounter++;
                }
                else{
                    tomatoCounter++;
                }
                cellCounter++;
            }
            else{
                isHoleDetected = true;
            }
        }
        if(mashroomCounter >= minIng && tomatoCounter >= minIng && !isHoleDetected){
            if(cellCounter > cellReallyCount){
                cellReallyCount = cellCounter;
                isCountLeft = false;
                isCountRight = false;
                isCountUp = false;
                isCountDown = true;
            }
        }
        cellCounter = 0;
        mashroomCounter = 0;
        tomatoCounter = 0;
        isHoleDetected = false;
	}
	
	public static void iterateToUp(int i, int j){
		 //iterating to up
        iterateLimit = i+1;
        if(iterateLimit > maxCells){
            iterateLimit = maxCells;
        }
        for(int l=0; l<iterateLimit; l++){
            if(slicedPizza[i-l][j] != '?'){
                if(slicedPizza[i-l][j] == 'M'){
                    mashroomCounter++;
                }
                else {
                    tomatoCounter++;
                }
                cellCounter++;
            }
            else{
                isHoleDetected = true;
            }
        }
        if(mashroomCounter >= minIng && tomatoCounter >= minIng && !isHoleDetected){
            if(cellCounter > cellReallyCount){
                cellReallyCount = cellCounter;
                isCountLeft = false;
                isCountRight = false;
                isCountUp = true;
            }
        }
        cellCounter = 0;
        mashroomCounter = 0;
        tomatoCounter = 0;
        isHoleDetected = false;
	}
	
	public static void iterateToRight(int i, int j){
		//iterating to right
		iterateLimit = columnNum-j;
        if(iterateLimit > maxCells){
            iterateLimit = maxCells;
        }
        for(int l=0; l<iterateLimit; l++){
            if(slicedPizza[i][j+l] != '?'){
                if(slicedPizza[i][j+l] == 'M'){
                    mashroomCounter++;
                }
                else{
                    tomatoCounter++;
                }
                cellCounter++;
            }
            else{
                isHoleDetected = true;
            }
        }
        if(mashroomCounter >= minIng && tomatoCounter >= minIng && !isHoleDetected){
            if(cellCounter > cellReallyCount){
                cellReallyCount = cellCounter;
                isCountLeft = false;
                isCountRight = true;
            }
        }
        cellCounter = 0;
        mashroomCounter = 0;
        tomatoCounter = 0;
        isHoleDetected = false;
	}
	
	public static void iterateToLeft(int i, int j){
        cellCounter = 0;
        mashroomCounter = 0;
        tomatoCounter = 0;
        isHoleDetected = false;
        cellReallyCount = 0;
        isCountLeft = false;
        isCountRight = false;
        isCountUp = false;
        isCountDown = false;
        //iterating to left
        iterateLimit = j+1;
        if(iterateLimit > maxCells){
            iterateLimit = maxCells;
        }
        for(int l=0; l<iterateLimit; l++){
        	/*if(i == 999 && j == 17){
        		System.out.println(slicedPizza[i][j-l]);
        	}*/
            if(slicedPizza[i][j-l] != '?'){
                if(slicedPizza[i][j-l] == 'M'){
                    mashroomCounter++;
                }
                else {
                    tomatoCounter++;
                }
                cellCounter++;
            }
            else{
                isHoleDetected = true;
            }
        }
        if(mashroomCounter >= minIng && tomatoCounter >= minIng && !isHoleDetected){
            cellReallyCount = cellCounter;
            isCountLeft = true;
        }
        cellCounter = 0;
        mashroomCounter = 0;
        tomatoCounter = 0;
        isHoleDetected = false;
    }
	
	public static void definitionOfSlice(int i, int j){
		String slice = "";
        if(isCountLeft){
            for(int l=0; l<cellReallyCount; l++){
                slicedPizza[i][j-l] = '?';
                slice += (i+","+(j-l)+" ");
            }
            slices.add(slice);
        }
        else if(isCountRight){
            for(int l=0; l<cellReallyCount; l++){
                slicedPizza[i][j+l] = '?';
                slice += (i+","+(j+l)+" ");
            }
            slices.add(slice);
        }
        else if(isCountUp){
            for(int l=0; l<cellReallyCount; l++){
                slicedPizza[i-l][j] = '?';
                slice += ((i-l)+","+j+" ");
            }
            slices.add(slice);
        }
        else if(isCountDown){
            for(int l=0; l<cellReallyCount; l++){
                slicedPizza[i+l][j] = '?';
                slice += ((i+l)+","+j+" ");
            }
            slices.add(slice);
        }
	}

    public static char [][] loadPizza(String file) {
        int row = 0;
        int column = 0;
        int counter = 0;
        char [][] pizzaMatrix = null;
        try (BufferedReader br=new BufferedReader(new FileReader(file))){
            String currentLine;
            while((currentLine = br.readLine()) != null){
                if(counter == 0){
                    String [] firstLine = currentLine.split(" ");
                    row = Integer.parseInt(firstLine[0]);
                    column = Integer.parseInt(firstLine[1]);
                    pizzaMatrix = new char[row][column];
                }
                else{
                    for(int i=0; i<currentLine.length(); i++){
                        pizzaMatrix[counter-1][i] = currentLine.charAt(i);
                    }
                }
                counter++;
            }
        }catch (Exception e){}
        return pizzaMatrix;
    }

    public static String loadRestrictions(String file) {
        int counter = 0;
        int minIng = 0;
        int maxCellNum = 0;
        try (BufferedReader br=new BufferedReader(new FileReader(file))){
            String currentLine;
            while((currentLine = br.readLine()) != null){
                if(counter == 0){
                    String [] firstLine = currentLine.split(" ");
                    minIng = Integer.parseInt(firstLine[2]);
                    maxCellNum = Integer.parseInt(firstLine[3]);
                }
                else{

                }
                counter++;
            }
        }catch (Exception e){}
        return String.valueOf(minIng)+" "+String.valueOf(maxCellNum);
    }
}
