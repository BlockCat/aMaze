package me.blockcat;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import me.blockcat.Main.Direction;

public class Grid {

	private Grid prevGrid = null;
	private List<Direction> openTo = new ArrayList<Direction>();
	private Color color = Color.white;
	public int x;
	public int y;

	public Grid(Grid previous, int x, int y) {
		this.prevGrid = previous;
		this.x = x;
		this.y = y;
	}

	public Grid toNextGrid(Grid[][] grids) {

		List<Direction> directions = Arrays.asList(Direction.values());
		Collections.shuffle(directions);

		for (Direction dir : directions) {
			int xn = x;
			int yn = y;
			if (dir == Direction.RIGHT) {
				xn++;
			} else if (dir == Direction.UP) {
				yn--;
			} else if (dir == Direction.LEFT) {
				xn--;
			} else if (dir == Direction.DOWN) {
				yn++;
			}

			if (yn > Main.HEIGTH-1 || yn < 0 || xn > Main.WIDTH-1 || xn < 0) {
				continue;
			}

			if (grids[xn][yn] == null) {
				openTo.add(dir);
				return new Grid(this, xn, yn);
			}
		}
		return null;
	}
	
	public Grid toRandNextGrid(Grid[][] grid, Grid[][] way) {
		Collections.shuffle(openTo);
		for (Direction dir : openTo) {
			int xn = x;
			int yn = y;
			if (dir == Direction.RIGHT) {
				xn++;
			} else if (dir == Direction.UP) {
				yn--;
			} else if (dir == Direction.LEFT) {
				xn--;
			} else if (dir == Direction.DOWN) {
				yn++;
			}

			if (yn > Main.HEIGTH-1 || yn < 0 || xn > Main.WIDTH-1 || xn < 0) {
				continue;
			}
			
			if (way[xn][yn] != null) {
				continue;
			} else {
				return grid[xn][yn];
			}
			
		}
		return null;
	}

	public Grid getPrev() {
		return this.prevGrid;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect((x * 16) + 2, (y * 16) + 2, 12, 12);
	}
	
	public void addBridges(Graphics g) {
		g.setColor(color);
		for (Direction dir : openTo) {
			if (dir == Direction.RIGHT) {
				g.fillRect((x*16) + 14,(y * 16) + 2 , 4, 12);
			} else if (dir == Direction.UP) {
				g.fillRect((x*16) + 2, (y * 16) - 2, 12, 4);
			} else if (dir == Direction.LEFT) {
				g.fillRect((x*16) - 2, (y * 16) + 2, 4, 12);
			} else if (dir == Direction.DOWN) {
				g.fillRect((x*16) + 2, (y * 16) + 14, 12, 4);
			}
		}
	}
}
