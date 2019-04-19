package combat.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import combat.items.Weapon;
import combat.ship.Room;
import combat.ship.Ship;
import banks.RoomSetBank;
import banks.WeaponBank;

public class EnemyGenerator {
	int crew;
	List<Room> roomsOut = new ArrayList<Room>();
    List<Weapon> weaponsOut = new ArrayList<Weapon>();
	int baseHullHp;
	
	public EnemyGenerator(int approxcrew,int approxhp){
		Random rng = new Random();
		
		int weaponcount = rng.nextInt(4);
		
		for(int i=0;i<=weaponcount;i++) {
			int randomwepnum = rng.nextInt(WeaponBank.values().length);
			Weapon wep = WeaponBank.values()[randomwepnum].getWeapon();
			if(wep.getName().equals("Boarding Harpoon")) {
				i--;
			}else {
				weaponsOut.add(wep);
			}
		}
		
		// these lines below randomly adjust the enemies hp and crew by upto 25% in each direction.
		float crewpercent = (float) (approxcrew*0.25);
		int crewchange = (int) (rng.nextInt((int) Math.floor(crewpercent))- Math.floor(crewpercent/2));
		this.crew = approxcrew + crewchange;
		
		float hppercent = (float) (approxhp*0.25);
		int hpchange = (int) (rng.nextInt((int) Math.floor(hppercent))- Math.floor(hppercent/2));
		this.baseHullHp = approxhp + hpchange;
		
		this.roomsOut = RoomSetBank.STARTER_ROOMS.getRoomList();
		
	}

	
	
	
	
	public Ship returnship() {
		return new Ship(this.crew, this.roomsOut, this.weaponsOut, this.baseHullHp);
	}
	
}
