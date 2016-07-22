package com.example.colze.utils.bingtu;
public class suanfa {
	public static double r8_abs(double x) {
		double value;

		if (0.0 <= x) {
			value = x;
		} else {
			value = -x;
		}
		return value;
	}

	public static double normal_01_cdf(double x) {
		double a1 = 0.398942280444;
		double a2 = 0.399903438504;
		double a3 = 5.75885480458;
		double a4 = 29.8213557808;
		double a5 = 2.62433121679;
		double a6 = 48.6959930692;
		double a7 = 5.92885724438;
		double b0 = 0.398942280385;
		double b1 = 3.8052E-08;
		double b2 = 1.00000615302;
		double b3 = 3.98064794E-04;
		double b4 = 1.98615381364;
		double b5 = 0.151679116635;
		double b6 = 5.29330324926;
		double b7 = 4.8385912808;
		double b8 = 15.1508972451;
		double b9 = 0.742380924027;
		double b10 = 30.789933034;
		double b11 = 3.99019417011;
		double cdf;
		double q;
		double y;
		if (r8_abs(x) <= 1.28) {
			y = 0.5 * x * x;

			q = 0.5 - r8_abs(x)
					* (a1 - a2 * y / (y + a3 - a4 / (y + a5 + a6 / (y + a7))));
		} else if (r8_abs(x) <= 12.7) {
			y = 0.5 * x * x;
			q = Math.exp(-y)
					* b0
					/ (r8_abs(x) - b1 + b2
							/ (r8_abs(x) + b3 + b4
									/ (r8_abs(x) - b5 + b6
											/ (r8_abs(x) + b7 - b8
													/ (r8_abs(x) + b9 + b10
															/ (r8_abs(x) + b11))))));
		} else {
			q = 0.0;
		}
		if (x < 0.0) {
			cdf = q;
		} else {
			cdf = 1.0 - q;
		}
		return cdf;
	}

	public static double normal_cdf(double x, double a, double b) {
		double cdf;
		double y;
		y = (x - a) / b;
		cdf = normal_01_cdf(y);
		return cdf;
	}
}
