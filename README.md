# Image2Image_converter


public static void showIndexesOfWinningCombination(){
		
		int index = 0;
		Map<String, Integer> map = new HashMap<>();
		
		for(int p1 = 1; p1 <= 38; p1++){
			for(int p2 = p1+1; p2 <= 39; p2++){
				for(int p3 = p2+1; p3 <= 40; p3++){
					for(int p4 = p3+1; p4 <= 41; p4++){
						for(int p5 = p4+1; p5 <= 42; p5++){
							for(int p6 = p5+1; p6 <= 43; p6++){
								for(int p7 = p6+1; p7 <= 44; p7++){
									String key = p1 + "," + p2 + "," + p3 + "," + p4 + "," + p5 + "," + p6 + "," + p7;;
									map.put(key,index++);
									
								}
							}
						}
					}
				}
			}
		}
		
		
		Set<String> seen = new HashSet<>();
		for(int i = 0 ; i < firstNums.length; i++){
			seen.add(firstNums[i] + "," + secondNums[i] + "," + thirdNums[i] + "," + fourthNums[i] + "," + fifthNums[i] + "," + sixthNums[i] + "," + seventhNums[i]);
		}

		for(String combination : seen){
			if(map.containsKey(combination)){
				System.out.println("Index: " + map.get(combination).intValue() + " = " + combination);
			}
		}
		
		
		
	}
	
