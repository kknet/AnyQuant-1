package ui.helper;

import java.lang.reflect.Field;
import java.util.List;

import enumeration.BenchMark_Attribute;
import enumeration.Stock_Attribute;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import vo.BenchMark;
import vo.Stock;

/**
 *
 * @author Qiang
 * @date 4/12/16
 */
public class ColorHelper {

	//
	public static Color red = new Color(238 / 255.0, 44 / 255.0, 44 / 255.0, 1.0);

	public static Color blue = new Color(45 / 255.0, 255 / 255.0, 254 / 255.0, 1.0);
	public static Color yellow = new Color(255 / 255.0, 253 / 255.0, 56 / 255.0, 1.0);
	public static Color green = new Color(50 / 255.0, 203 / 255.0, 62 / 255.0, 1.0);

	public ColorHelper() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setColorForStock(final List observableList,
			final ObservableList<TableColumn<Stock, ?>> observableList2) {

		for (int i = 0; i < observableList2.size(); i++) {
			TableColumn column = observableList2.get(i);
			String name = column.getId();

			column.setCellFactory(new Callback<TableColumn, TableCell>() {
				@Override
				public TableCell call(TableColumn param) {
					return new TableCell() {
						@Override
						protected void updateItem(Object item, boolean empty) {
							super.updateItem(item, empty);
							if (!isEmpty()) {
								this.setTextFill(ColorHelper.getColor((Stock) observableList.get(getIndex()), name));
								if (item instanceof String) {
									setText((String) item);
								} else if (item instanceof Double) {
									setText(((Double) item).toString());
								} else if (item instanceof Long) {
									setText(((Long) item).toString());
								} else {
									setText(String.valueOf(item));
								}

							}
						}
					};

				}
			});
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setColorForBench(final List observableList,
			final ObservableList<TableColumn<BenchMark, ?>> observableList2) {

		for (int i = 0; i < observableList2.size(); i++) {
			TableColumn column = observableList2.get(i);
			String name = column.getId();
			column.setCellFactory(new Callback<TableColumn, TableCell>() {
				@Override
				public TableCell call(TableColumn param) {
					return new TableCell() {
						@Override
						protected void updateItem(Object item, boolean empty) {
							super.updateItem(item, empty);
							if (!isEmpty()) {
								this.setTextFill(
										ColorHelper.getColor((BenchMark) observableList.get(getIndex()), name));

								if (item instanceof String) {
									setText((String) item);
								} else if (item instanceof Double) {
									setText(((Double) item).toString());
								} else if (item instanceof Long) {
									setText(((Long) item).toString());
								} else {
									setText(String.valueOf(item));
								}

							}
						}
					};

				}
			});
		}

	}

	public static Color getColor(Stock stock, String types) {

		return getColor(stock, Stock_Attribute.valueOf(types));

	}

	public static Color getColor(BenchMark benchMark, String types) {
		return getColor(benchMark, BenchMark_Attribute.valueOf(types));
	}

	private static Color getColor(BenchMark benchMark, BenchMark_Attribute attribute) {
		double preclose = benchMark.preClose.doubleValue();
		try {
			Class<?> temp = Class.forName("vo.BenchMark");

			Field field = temp.getDeclaredField(attribute.name());
			field.setAccessible(true);
			switch (attribute) {
			case high:
			case low:
			case open:
			case close:

				return getColor(((SimpleDoubleProperty) field.get(benchMark)).doubleValue(), preclose);
			// case changeRate:
			// return
			// getColor(((SimpleDoubleProperty)field.get(benchMark)).doubleValue(),
			// 0);

			default:
				return Color.WHITE;
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return Color.BLUE;

	}

	private static Color getColor(Stock stock, Stock_Attribute attribute) {
		double preclose = stock.preClose.doubleValue();
		try {
			Class<?> temp = Class.forName("vo.Stock");

			Field field = temp.getDeclaredField(attribute.name());
			field.setAccessible(true);
			switch (attribute) {
			case high:
			case low:
			case open:
			case close:

				return getColor(((SimpleDoubleProperty) field.get(stock)).doubleValue(), preclose);
			case changeRate:
				return getColor(((SimpleDoubleProperty) field.get(stock)).doubleValue(), 0);
			case turnoverVol:
				return yellow;
			case turnoverRate:
			case amplitude:
				return blue;

			default:
				return Color.WHITE;
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return Color.BLUE;

	}

	private static Color getColor(double value, double standard) {

		if (value > standard) {
			return red;
		} else if (value < standard) {
			return green;
		} else {
			return Color.WHITE;
		}
	}

}