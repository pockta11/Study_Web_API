package kr.project;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// ItemListener : 달력 날짜 년, 월 Choice 이벤트
// ActionListener : 일에 대한 Choice 이벤트
public class Project02_C extends JFrame implements ActionListener, ItemListener {

	private Choice chyear, chmonth;
	private JLabel yLabel, mLabel;
	private JTextArea area;
	GregorianCalendar gc;
	private int year, month;
	private JLabel[] dayLabel = new JLabel[7];
	private String[] day = { "일", "월", "화", "수", "목", "금", "토" };
	private JButton[] days = new JButton[42];// 7일이 6주이므로 42개의 버튼필요
	private JPanel selectPanel = new JPanel();
	private GridLayout grid = new GridLayout(7, 7, 5, 5);// 행,열,수평갭,수직갭
	private Calendar ca = Calendar.getInstance();
	private Dimension dimen, dimen1;
	private int xpos, ypos;

	public Project02_C() {
		/* Calender.MONTH + 1 : 월이 0부터 시작하므로 + 1 해줌 */
		setTitle("오늘의 말씀 QT : " + ca.get(Calendar.YEAR) + "/");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dimen = Toolkit.getDefaultToolkit().getScreenSize(); // 화면의 크기
		dimen1 = this.getSize(); // 프레임의 크기
		xpos = (int) (dimen.getWidth() / 2 - dimen1.getWidth() / 2); /* 화면의 가로 가운데 */
		ypos = (int) (dimen.getHeight() / 2 - dimen1.getHeight() / 2); /* 화면의 세로 가운데 */
		setLocation(xpos, ypos);// 화면의 가운데에 출력
		setResizable(false); // 창크기 고정
		setVisible(true); // 화면에 창보이기
		chyear = new Choice();
		chmonth = new Choice(); // new Choice():컬렉션중 하나의 값을 리턴
		yLabel = new JLabel("년");
		mLabel = new JLabel("월");
		init();
	}

	public void actionPerformed(ActionEvent arg0) {
		area.setText("");
		String year = chyear.getSelectedItem(); // getSelectedItem():선택된 객체의 내용
		String month = chmonth.getSelectedItem();
		JButton btn = (JButton) arg0.getSource();
		String day = btn.getText();// getText():메소드로 현재 텍스트박스내의 문자열을 읽어
		System.out.println(year + "," + month + "," + day);
		String bible = year + "-" + month + "-" + day;

		// Jsoup API : HTML파싱 방법
		// String url="https://sum.su.or.kr:8888/bible/today";
		String url = "https://sum.su.or.kr:8888/bible/today/Ajax/Bible/BodyMatter?qt_ty=QT1&Base_de=" + bible
				+ "&bibleType=1";
		try {
			Document doc = Jsoup.connect(url).post();
			// System.out.println(doc.toString());
			Element bible_text = doc.select(".bible_text").first();
			System.out.println(bible_text.text());
			Element bibleinfo_box = doc.select("#bibleinfo_box").first();
			System.out.println(bibleinfo_box.text());
			// dailybible_info
			Element dailybible_info = doc.select("#dailybible_info").first();
			System.out.println(dailybible_info.text());
			area.append(dailybible_info.text() + "\n");
			area.append(bible_text.text() + "\n");
			area.append(bibleinfo_box.text() + "\n");
			Elements liList = doc.select(".body_list > li");
			for (Element li : liList) {
				String line = li.select(".info").first().text();
				if (line.length() > 65) {
					line = line.substring(0, 36) + "\n" + line.substring(36, 66) + "\n" + line.substring(66) + "\n";
					area.append(li.select(".num").first().text() + ":" + line);
				} else if (line.length() > 35) {
					line = line.substring(0, 36) + "\n" + line.substring(36) + "\n";
					area.append(li.select(".num").first().text() + ":" + line);
				} else {
					area.append(li.select(".num").first().text() + ":" + li.select(".info").first().text() + "\n");
				}
				System.out.print(li.select(".num").first().text() + ":");
				System.out.println(li.select(".info").first().text());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void init() {
		select(); // 년, 월 선택
		calender(); // 일 선택
	}

	public void select() {
		JPanel panel = new JPanel(grid);// 7행 7열의 그리드레이아웃
		for (int i = 2023; i >= 2000; i--) {
			chyear.add(String.valueOf(i));
		}
		for (int i = 1; i <= 12; i++) {
			chmonth.add(String.valueOf(i));
		}
		for (int i = 0; i < day.length; i++) {// 요일 이름을 레이블에 출력
			dayLabel[i] = new JLabel(day[i], JLabel.CENTER);
			panel.add(dayLabel[i]);
			dayLabel[i].setBackground(Color.GRAY);// 사실상 의미가 없음. 바뀌지 않음.
		}
		dayLabel[6].setForeground(Color.BLUE);// 토요일 색상
		dayLabel[0].setForeground(Color.RED);// 일요일 색상
		for (int i = 0; i < 42; i++) {// 모두 42개의 버튼을 생성
			days[i] = new JButton("");// 제목이 없는 버튼 생성
			if (i % 7 == 0)
				days[i].setForeground(Color.RED);// 일요일 버튼의 색
			else if (i % 7 == 6)
				days[i].setForeground(Color.BLUE);// 토요일 버튼의 색
			else
				days[i].setForeground(Color.BLACK);
			days[i].addActionListener(this);
			panel.add(days[i]);
		}
		selectPanel.add(chyear);
		selectPanel.add(yLabel);
		selectPanel.add(chmonth);
		selectPanel.add(mLabel);
		// JPanel bib=new JPanel();
		area = new JTextArea(60, 40);
		area.setCaretPosition(area.getDocument().getLength());
		JScrollPane scrollPane = new JScrollPane(area);
		this.add(selectPanel, "North");// 연도와 월을 선택할 수 있는 화면 상단에 출력
		this.add(panel, "Center");
		this.add(scrollPane, "East");

		String m = (ca.get(Calendar.MONTH) + 1) + "";
		String y = ca.get(Calendar.YEAR) + "";
		chyear.select(y);
		chmonth.select(m);
		chyear.addItemListener(this);
		chmonth.addItemListener(this);
	}

	public void calender() {
		year = Integer.parseInt(chyear.getSelectedItem());
		month = Integer.parseInt(chmonth.getSelectedItem());
		gc = new GregorianCalendar(year, month - 1, 1); // 달에 마지막 날짜 구하기
		int max = gc.getActualMaximum(gc.DAY_OF_MONTH);// 해당 달의 최대 일 수 획득
		int week = gc.get(gc.DAY_OF_WEEK);// 해당 달의 시작 요일
//	      System.out.println("DAY_OF_WEEK:"+week);
		String today = Integer.toString(ca.get(Calendar.DATE));// 오늘 날짜 획득
		String today_month = Integer.toString(ca.get(Calendar.MONTH) + 1);// 오늘의 달 획득
//	      System.out.println("today:"+today);
		for (int i = 0; i < days.length; i++) {
			days[i].setEnabled(true);
		}
		for (int i = 0; i < week - 1; i++) {// 시작일 이전의 버튼을 비활성화
			days[i].setEnabled(false);
		}
		for (int i = week; i < max + week; i++) {
			days[i - 1].setText((String.valueOf(i - week + 1)));
			days[i - 1].setBackground(Color.WHITE);
			if (today_month.equals(String.valueOf(month))) {// 오늘이 속한 달과 같은 달일 경우
				if (today.equals(days[i - 1].getText())) {// 버튼의 날짜와 오늘날짜가 일치하는 경우
					days[i - 1].setBackground(Color.CYAN);// 버튼의 배경색 지정
				}
			}
		}
		for (int i = (max + week - 1); i < days.length; i++) {// 날짜가 없는 버튼을 비활성화
			days[i].setEnabled(false);
		}
//	    System.out.println("max+week:"+(max+week)+",week:"+week);
	}

	public void itemStateChanged(ItemEvent e) {

	}

	public static void main(String[] args) {
		new Project02_C();
	}

}
