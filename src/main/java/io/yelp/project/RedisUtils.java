package io.yelp.project;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RedisUtils {
	private static final Random random = new Random();

	public static final List<String> keys = List.of(
			"business:28EJ38U2GnW6ePPw9OKcVA",
			"business:5MNZPN-tFV-GfocZgSlD9A",
			"business:a7HgmqTBM3aFIg7UhxK8yA",
			"business:AffTEkOQxv-k__8SeIvQnw",
			"business:AVaoaSMx3kRO_vPG1fulvw",
			"business:biQ2MEsJxTsqwx6Ianb6cw",
			"business:DJP7FIzZnLBWlVCNQne1ew",
			"business:hURamQdtk1WRYVECzz-Sfw",
			"business:J_NA-_WNLAJQg7eZwn1dWw",
			"business:K-FdQFWubjy8RKkeQP1zYg",
			"business:NXeXE0rYkDmAH2rc_hbFLg",
			"business:raDe1hbHx0z52LdZuyIzAA",
			"business:ry1HfPtoF2EcH8vURvc9cA",
			"business:svTDpnD6yj77h5iH9lDucw",
			"business:uWUGJVpsACeHqtiigWC26g",
			"review:2G6ugDQhC7qbY3Qb-11Euw",
			"review:5KFBIzvaMDMCVScEUlOb0Q",
			"review:c3PjfRSPbio4GWW4ePM05Q",
			"review:C90gQmxVBwHSggekPxzzCA",
			"review:E-h7laSwR2nVCx1x1DfETw",
			"review:elPQEXblUT_bA7e9ervKFg",
			"review:ePJHiCpHOgb0ua-sx4yKQQ",
			"review:FKh1AFCdkMjl9FjzaK1BlA",
			"review:Fw70sJ0T7LYc35YkzaiW7w",
			"review:gCpawIvPOo1_xNEXQ69_1g",
			"review:hJTBbcMR2CDauhQwmM5eZw",
			"review:hzuZgujuvMZu6cVFyvEiVA",
			"review:I1gpSifNBZ5ZcHVCuj8VIg",
			"review:iMMvcLSEkmy68dhUEvR3XQ",
			"review:jjBjvAw7moddLN3XruOoDQ",
			"review:JxIsl7TuQf0B93itDAEuGw",
			"review:MADnYeseoz8NmcpEyShLAQ",
			"review:nd3GWeXN6w9XqMPyMAAswQ",
			"review:oD7kw4bfBQ-h9MlnyX9j1A",
			"review:OV-n6ROvd1p5pxYxohE_Gg",
			"review:QBmmh3Cz8Ufhc_x7JKvA1A",
			"review:rEQ0fZ5aSc3vHJU5MnDPiA",
			"review:RGKI3V4_NsQLzKQHT0asZw",
			"review:rtBXlzXPngwahDX9eMjK6w",
			"review:sd17NYdNpSjo7TkehGq2CQ",
			"review:t1DTRwXAWHfsVnIAwMVx4A",
			"review:uu9UvNZkdmZFzjxGXr9Qvw",
			"review:VaGXYcCaou1wCu4dl3Az-Q",
			"review:vwlNg4BKlyRQOh8SGymFMg",
			"review:Wl0mthxfHNS1vZID8WHnCg",
			"review:XQB3rjOxdzvFkjjNDDThTA",
			"review:YiEnc0MBotp42BynpAX1Eg",
			"review:z3lv-aa7eMDryc2KZNhV9A",
			"review:z5DhYHtIEiKxVq7FIsKnQQ",
			"review:ZpGfHFDT4KcfhfwK7emaUQ",
			"user:3F3MhzYACZ_b5plIr_Rbpg",
			"user:9tfpuzR0C6XoZYQX-BXrtg",
			"user:bq8rqm2Jr5LpH-pqXqX1PQ",
			"user:Bt1uYwJH_TwrVO7J_yxWQg",
			"user:DpbQMqF6OVdxURD-jPiwnA",
			"user:g6s-cwqUgCvSQAKHbpoLPw",
			"user:IhbFMYAVstF1kENR6V5lQw",
			"user:KitAzvCdBhRDDLFy5du8HQ",
			"user:M1X8UfTUF9LBsa1_BiiNCA",
			"user:NWth2xdGjVdc_L1UVWU9UQ",
			"user:oBeeGApPBaYyH4xr66nYyg",
			"user:pmy2Y7CazBa3t_469KYAbw",
			"user:scR-umlMYuISWLdJxZmcdw",
			"user:TA_W6lPXVC3JKR3_lndD0g",
			"user:xHdbC2dOjuOaYq973lI9Xw",
			"user:Y0eMvIt5NRdNZsa7RbWhXA",
			"user:YcBWaiXj_LG3zXLZrkzxGg",
			"user:Ywf0cmVx1ySpAjRCihoSEg"
	);

	public static String getRandomNonExistingKey() {
		return UUID.randomUUID().toString();
	}

	public static String getRandomValue() {
		return UUID.randomUUID().toString();
	}

	public static String getRandomExistingKey() {
		return keys.get(random.nextInt(keys.size()));
	}

	public static boolean shouldBeGetQuery() {
		return random.nextBoolean();
	}

	public static boolean shouldBeRangeQuery() {
		return random.nextBoolean();
	}
}
