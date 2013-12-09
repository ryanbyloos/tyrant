package mikera.tyrant.test;

import java.util.HashMap;

import mikera.tyrant.*;
import mikera.tyrant.author.MapMaker;
import mikera.tyrant.engine.Map;
import junit.framework.TestCase;


public class TestMapMaker extends TestCase {
    private MapMaker mapMaker;
    private StringBuffer actual;
    
    private static final String NL=MapMaker.NL;

    protected void setUp() throws Exception {
        actual = new StringBuffer();
        mapMaker = new MapMaker();
    }
    
    public void testCycle() {
    	Map map=new Map(5,5);
    	map.fillArea(0,0,4,4,Tile.FLOOR);
    	map.addThing("rat",1,1);
    	map.addThing("gold coin",2,2);
    	map.addThing("IsHostile@",3,3);
    	
    	String mapText=mapMaker.store(map);
    	
    	map=mapMaker.create(mapText, true);
    	assertNotNull(map);
    	
    	String newMapText=mapMaker.store(map);

    	assertEquals(mapText,newMapText);
    }
    
    public void testStore() throws Exception {
        Map map = new Map(3, 3);
        map.fillArea(0, 0, map.getWidth(), map.getHeight(), Tile.CAVEFLOOR);
        String expected =
            "---Tiles---" + NL +
            "aaa" + NL +
            "aaa" + NL +
            "aaa" + NL +
            "---Tiles---" + NL;
        mapMaker.storeTiles(map, actual);
        assertEquals(expected, actual.toString());
    }
    
    public void testStore2() throws Exception {
        Map map = new Map(3, 3);
        map.fillArea(0, 0, map.getWidth(), map.getHeight(), Tile.CAVEFLOOR);
        map.fillArea(1, 1, 1, 1, Tile.ICEFLOOR);
        String expected =
            "---Tiles---" + NL +
            "aaa" + NL +
            "aba" + NL +
            "aaa" +NL +
            "---Tiles---" + NL;
        mapMaker.storeTiles(map, actual);
        assertEquals(expected, actual.toString());
    }
    
    public void testStore3() throws Exception {
        Map map = new Map(3, 1);
        map.fillArea(1, 0, 1, 0, Tile.ICEFLOOR);
        map.fillArea(2, 0, 2, 0, Tile.LAVA);
        String expected =
            "---Tiles---" + NL +
            "abc" + NL +
            "---Tiles---" + NL;
        mapMaker.storeTiles(map, actual);
        assertEquals(expected, actual.toString());
    }
    
    public void testLegend() throws Exception {
        Map map = new Map(3, 3);
        map.fillArea(0, 0, map.getWidth(), map.getHeight(), Tile.CAVEFLOOR);
        mapMaker.store(map);
        assertMapEquals("a = cave floor", mapMaker.getLegend());
    }

    public void testLegend2() throws Exception {
        Map map = new Map(5, 5);
        int i = 0;
        for (int x = 0; x < map.getWidth(); x++) 
            for(int y = 0; y < map.getHeight(); y++) 
                map.setTile(x, y, i++);
        mapMaker.store(map);
        assertEquals(25, mapMaker.getLegend().size());
    }

    private void assertMapEquals(String toMap, java.util.Map<String,String> map) {
        HashMap<String, String> expected = new HashMap<>();
        String[] splits = toMap.split("=");
        for (int i = 0; i < splits.length; i+=2) {
            String string = splits[i];
            expected.put(string.trim(), splits[i + 1].trim());
        }
        assertEquals(expected, map);
    }
    
    public void testMake() throws Exception {
        String mapText = 
            "---Tiles---" + NL +
            "abcd" + NL +
            "---Tiles---" + NL +
            "" + NL +
            "---Legend---" + NL +
            "a = grass" + NL +
            "b = wall" + NL +
            "c = stone floor" + NL +
            "d = posh floor" + NL +
            "Width = 4" +NL +
            "Height = 1" +NL +
            "EntranceX = 1" +NL +
            "EntranceY = 1" +NL +
            "---Legend---";
        Map map = mapMaker.create(mapText, true);
        assertEquals(Tile.GRASS, map.getTile(0, 0));
        assertEquals(Tile.WALL, map.getTile(1, 0));
        assertEquals(Tile.STONEFLOOR, map.getTile(2, 0));
        assertEquals(Tile.POSHFLOOR, map.getTile(3, 0));
    }
}
