/* Naïve Bayes Classifier */
public class NBC
{

	public static void runTests(String[] args)
	{
		/*
		sex     height (feet)   weight (lbs)  foot size(inches)
		male         6             180               12
		male      5.92 (5'11")     190               11
		male      5.58 (5'7")      170               12
		male      5.92 (5'11")     165               10
		female       5             100                6
		female     5.5 (5'6")      150                8
		female    5.42 (5'5")      130                7
		female    5.75 (5'9")      150                9

		sex     mean (height)   variance (height)   mean (weight)   variance (weight)  mean (foot size)  variance (foot size)
        male    5.855           3.5033e-02          176.25          1.2292e+02         11.25             9.1667e-01
        female  5.4175          9.7225e-02          132.5           5.5833e+02         7.5               1.6667e+00

		Testing:
		sex     height (feet)   weight (lbs)  foot size(inches)
		sample       6             130        8

		*/
	}

	public static void main (String[] args)
	{
		runTests(args);
	}
}
