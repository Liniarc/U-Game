package main;

import objects.BigInt;

public class UniverseData {

	public boolean[] eraAvailable = new boolean[2];
	
	/**
	 * Counters
	 */
//	public double energy;
//	public int energyMag;
	public BigInt energy = new BigInt(0);
	
	public double up, down, top, bottom, strange, charm;

	public double proton, neutron, lambda, sigma, xi, delta, omega;
	public double pion, kaon, photon, neutrino, muon;

	public double deuterium, alphaP;

	public double hydrogen, helium;


	// Buildings
	/**
	 * Era 0 upgrades<br>
	 * 0. Click multiplier<br>
	 * 1. Auto multiplier<br>
	 * 2. Builder<br>
	 */
	public BigInt[] era0buildings = {new BigInt(1),new BigInt(1),new BigInt(1)};
	
	
	// Upgrades
	 
	/**
	 * Era 0 upgrades<br>
	 * 0. Stronger clicks<br>
	 * 1. Dragging<br>
	 * 2. Auto clicks<br>
	 * 3. Auto click boost<br>
	 * 4. Half off<br>
	 * 5. boost max length<br>
	 * 6. boost strength<br>
	 * 7. More auto energy<br>
	 * 8. Unlock Builder<br>
	 * 9. Auto builder<br>
	 * 10. build auto builder<br>
	 * 11. auto build builder
	 */
	public boolean[] era0upgrades = new boolean[12];
	
	/**
	 * Achievements
	 */
	public boolean[] era0achievements = new boolean[10];
	
	public void calculations()
	{	
	}
}
