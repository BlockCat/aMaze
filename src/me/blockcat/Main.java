package me.blockcat;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Main extends BasicGame {

	public static final int WIDTH = 100;
	public static final int HEIGTH = 60;
	private Image mazeImage = null;
	private Image wayImage = null;

	public Main(String title) {
		super(title);
	}

	private Grid[][] grid = new Grid[WIDTH][HEIGTH];
	private Grid[][] way = new Grid[WIDTH][HEIGTH];
	private int startX = 0;
	private int startY = 0;
	private static Main main = null;

	public static void main(String[] args) {
		main = new Main("aMAZEing");
		main.carveSpace();
		AppGameContainer game;
		try {
			game = new AppGameContainer(main);
			game.setDisplayMode(WIDTH * 16, HEIGTH * 16, false);
			game.setShowFPS(false);
			game.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	public boolean carveSpace() {
		Grid g = new Grid(null, startX, startY);
		grid = new Grid[WIDTH][HEIGTH];
		while (true) {
			if (g != null) {
				grid[g.x][g.y] = g;
				Grid tempG = g.toNextGrid(grid);
				//if null then no valid space, go back
				if (tempG == null) {					
					g = g.getPrev();
				} else {
					g = tempG;
					continue;
				}
			} else {
				if (g == null) {
					makeImage();
					return true;
				}
			}
		}
	}

	private void makeImage() {

	}

	private boolean shallfindWay = false;
	private Grid fGrid = null;

	public boolean findWay() {
		for (int y = 0; y < HEIGTH; y++) {
			for (int x = 0; x < WIDTH; x++) {
				grid[x][y].setColor(Color.white);
			}
		}

		way = new Grid[WIDTH][HEIGTH];
		Grid g = grid[startX][startY];
		g.setColor(Color.red);
		fGrid = g;
		shallfindWay = true;
		return true;
		/*while(true) {
			if (g != null) {
				way[g.x][g.y] = g;
				Grid tempG = g.toRandNextGrid(grid, way);
				//if null then no valid space, go back
				if (tempG == null) {
					g.setColor(Color.white);
					g = g.getPrev();
					continue;
				} else {
					g = tempG;
					g.setColor(Color.red);

					if (g.x == this.WIDTH - 1 && g.y == this.HEIGTH -1) {
						this.makeImage();
						return true;
					}

					continue;
				}
			} else {
				if (g == null) {
					return true;
				}
			}
		}*/
	}

	public enum Direction {
		RIGHT, UP, LEFT, DOWN;

		public static Direction getRandom() {
			Random r = new Random();
			int ran = r.nextInt(4);
			switch(ran) {
			case 0: 
				return Direction.RIGHT;
			case 1: 
				return Direction.UP;
			case 2: 
				return Direction.LEFT;
			case 3: 
				return Direction.DOWN;
			}
			return Direction.DOWN;
		}
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH * 16, HEIGTH * 16);
		for (int y = 0; y < HEIGTH; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if (this.grid[x][y] != null) {
					this.grid[x][y].render(g);
				} else {
				}
			}
		}
		for (int y = 0; y < HEIGTH; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if (this.grid[x][y] != null) {
					this.grid[x][y].addBridges(g);
				} else {
				}
			}
		}

		if (findWay && mazeImage == null) {
			//mazeImage = g.
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {

	}
	private boolean keyboard = true;
	private boolean findWay = true;
	private int countDown = 30;

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			if (keyboard) {
				this.shallfindWay = false;
				main.carveSpace();
				keyboard = false;
			}
		} else {
			keyboard = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			if (findWay) {
				main.findWay();
				findWay = false;
			}
		} else {
			findWay = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			this.finishWay();
		}

		if (this.shallfindWay) {
			if (this.countDown <= 0) {
				this.fGrid = findWayStep(fGrid);
				if (fGrid.x == WIDTH - 1 && fGrid.y == HEIGTH -1) {
					this.shallfindWay = false;
				}
				countDown = 0;
			} else {
				countDown--;
			}
		}
	}

	private void finishWay() {
		for (int y = 0; y < HEIGTH; y++) {
			for (int x = 0; x < WIDTH; x++) {
				grid[x][y].setColor(Color.white);
			}
		}

		way = new Grid[WIDTH][HEIGTH];
		Grid g = grid[startX][startY];
		g.setColor(Color.red);
		fGrid = g;

		while(true) {
			this.fGrid = findWayStep(fGrid);
			if (fGrid != null) {
				if (fGrid.x == WIDTH - 1 && fGrid.y == HEIGTH -1) {
					return;
				}
			}
		}
	}

	private Grid findWayStep(Grid g) {
		if (g != null) {
			way[g.x][g.y] = g;
			Grid tempG = g.toRandNextGrid(grid, way);

			if (tempG == null) {
				g.setColor(Color.white);
				g = g.getPrev();
			} else {
				g = tempG;
				g.setColor(Color.red);

				if (g.x == WIDTH - 1 && g.y == HEIGTH -1) {
				}
			}
			return g;
		}
		return null;
	}

}
