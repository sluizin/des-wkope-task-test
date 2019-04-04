package des.wangku.operate.task.test;

import java.io.File;
import java.sql.Connection;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import des.wangku.operate.standard.PV;
import des.wangku.operate.standard.database.MainSource;
import des.wangku.operate.standard.swt.DBACCESSCTabFolder;
import des.wangku.operate.standard.swt.DBMYSQLCTabFolder;
import des.wangku.operate.standard.swt.ExcelCTabFolder;
import des.wangku.operate.standard.swt.ResultTable;
import des.wangku.operate.standard.task.AbstractTask;
import des.wangku.operate.standard.utls.UtilsReadURL;
import des.wangku.operate.standard.utls.UtilsRnd;
import des.wangku.operate.standard.utls.UtilsSWTPOI;
import des.wangku.operate.standard.utls.UtilsSWTTableSQL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * UI测试平台
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class WKOPETaskTest extends AbstractTask {
	/** 日志 */
	static Logger logger = LoggerFactory.getLogger(WKOPETaskTest.class);
	final Display display = Display.getDefault();
	WKOPETaskTest base = this;
	ExcelCTabFolder excelCTabFolder = null;
	DBMYSQLCTabFolder dbmysqlCTabFolder = null;
	DBACCESSCTabFolder dbaccessCTabFolder = null;
	ExpandBar expandBar = null;
	private Text sqlinput;
	private Text txtNew;

	public WKOPETaskTest(Composite parent) {
		super(parent);
	}

	public WKOPETaskTest(Composite parent, int style) {
		super(parent, style, WKOPETaskTest.class, AbstractTask.MVver | AbstractTask.MVmodQuit | AbstractTask.MVsysQuit);
		excelCTabFolder = new ExcelCTabFolder(this, SWT.NONE);
		excelCTabFolder.initialization();
		excelCTabFolder.setBounds(10, 10, 868, 170);
		dbmysqlCTabFolder = new DBMYSQLCTabFolder(this, SWT.None, "0", this.getProProperties(), MainSource.getConnWKjixiao());
		dbmysqlCTabFolder.setBounds(10, 358, 868, 56);


		dbaccessCTabFolder = new DBACCESSCTabFolder(this, SWT.None, "0",base);
		dbaccessCTabFolder.setBounds(10, 275, 868, 65);
		
		
		Group grpSql = new Group(this, SWT.NONE);
		grpSql.setText("SQL");
		grpSql.setBounds(10, 420, 868, 130);

		sqlinput = new Text(grpSql, SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		String sql="select id as '序号',depart_name as '部门',email,is_delete as '删除',leader_id as '上级id',mobile_phone as '手机',`name` as '姓名'," + 
				"`no` as '公司编号',organization_name_two as '二级部门',organization_name as '部门',post as '职位',user_type as '人员类型'," + 
				"leader_nos as '领导等级' from user limit 10";
		sqlinput.setText(sql);
		sqlinput.setBounds(10, 20, 328, 100);
		/*GridData gd_cmdIntro = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
		        1);
		gd_cmdIntro.widthHint = 100; // 必须得设置宽度，否则自动换行不好使
		gd_cmdIntro.heightHint = 200;
		sqlinput.setLayoutData(gd_cmdIntro);*/
		
		
		
		Button btnNewButton = new Button(grpSql, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String sql = sqlinput.getText();
				String tablename=txtNew.getText();
				String jsonStr=sqlparainput.getText();
				dbmysqlCTabFolder.addDBResultTable(tablename, sql,jsonStr);
				dbmysqlCTabFolder.putPopMenu();
			}
		});
		btnNewButton.setBounds(356, 54, 68, 23);
		btnNewButton.setText("生成表");
		
		txtNew = new Text(grpSql, SWT.BORDER);
		txtNew.setText("new");
		txtNew.setBounds(357, 20, 102, 19);
		
		sqlparainput = new Text(grpSql, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		sqlparainput.setBounds(677, 17, 181, 103);
		sqlparainput.setText(jsonStr);
		
		Button btnNewButton_1 = new Button(grpSql, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Connection conn = MainSource.getConnection("wk_sys");
				String sql="select b.name as '地区',a.alias as '核心单品',a.name as '产业网名称',replace(a.url,'http://','') as '二级域名',a.id as '产业网ID' from wk_sys.site a, wk_trade_shard1.sys_area b where left(a.area_code,6)=b.area_code and a.dp_platform_type='cyw' and a.open_status=0 and enable=0 order by left(a.area_code,6) asc";
				String path = PV.getJarBasicPath() + "/" + PV.ACC_OutputCatalog;
				String filename = UtilsRnd.getNewFilenameNow(4, 1) + ".xlsx";
				File file = new File(path);
				if (!file.exists()) file.mkdirs();
				String excelFilename = path + "/" + filename;
				UtilsSWTPOI.makeExcel(excelFilename, "所有产业网", conn, sql, true);
			}
		});
		btnNewButton_1.setBounds(499, 15, 68, 23);
		btnNewButton_1.setText("New Button");
		
		Button button = new Button(grpSql, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultTable  f=excelCTabFolder.getSelectResultTable();
				int[] checkarr=f.getCheckedTableItemSuffix();
				for(int i=0;i<checkarr.length;i++) {
					int suffix=checkarr[i];
					String url="http://"+f.getString(suffix,3);
					logger.debug("url:"+url);
					int result=UtilsReadURL.getUrlResponseContentLength(url, 30000);
					f.setString(suffix, 5, result+"");
				}
				
			}
		});
		button.setBounds(453, 86, 68, 23);
		button.setText("检测");
		
		Button btnNewButton_2 = new Button(this, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultTable rt=excelCTabFolder.getSelectResultTable();
				//String filename="D:\\Eclipse\\eclipse-oxygen\\Workspaces\\des-wkope\\build\\libs\\model\\des-wkope-task-p001.xlsx";
				//Sheet sheet=UtilsSWTPOI.getSheet(filename, 1);
				for(int i=0;i<rt.getItemCount();i++) {
					String name=rt.getString(i, 0);
					String url="https://baike.baidu.com/item/"+name;
					String value=null;
					try {
						String content=UtilsReadURL.getReadUrlDisJs(url);
						Document doc=Jsoup.parse(content);
						//Document doc=UtilsJsoup.getDoc(new URL(url),1);
						Elements els=doc.getElementsByClass("lemma-summary");
						if(els.size()==0)continue;
						System.out.println(name+":"+els.size());
						Element el=els.first();
						value=el.text();
						UtilsSWTTableSQL.update(rt, i, 1,value);
						
						value=getParaText(doc,"名优特产");
						UtilsSWTTableSQL.update(rt, i, 2,value);

						value=getParaText(doc,"地方特产");
						UtilsSWTTableSQL.update(rt, i, 3,value);
						value=getParaText(doc,"地方文化");
						UtilsSWTTableSQL.update(rt, i, 4,value);
						value=getParaText(doc,"地方特色");
						UtilsSWTTableSQL.update(rt, i, 5,value);
						
						
						
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				/*
				for(int i=0;i<rt.getItemCount();i++) {
					String url=rt.getString(i, 10);
					//String url=UtilsSWTPOI
					logger.debug("url:"+url);
					int x=UtilsSWTPOI.getSearchRowsKeyEquals(sheet, url, 0, 3);
					if(x!=-1) {
						String value=UtilsSWTPOI.getCellValueByString(sheet, x, 4, false, null);
						if(value!=null)
						logger.debug("value:"+value);
						UtilsSWTTableSQL.update(rt, i, 15,value);
					}
					
				}*/
				
				
				
				
				
				
				
				
				
				
				
				
			}
			
			
			
			
			
			
		});
		btnNewButton_2.setBounds(43, 200, 68, 23);
		btnNewButton_2.setText("New Button");
		
	}
	static final String getParaText(Document doc,String chnkey) {
		Elements arrs=doc.getElementsByClass("para-title");
		for(Element ee:arrs) {
			if(ee.text().indexOf(chnkey)>-1) {	
				Element next=ee.nextElementSibling();
				StringBuilder sb=new StringBuilder();
				while(next.hasClass("para")) {
					sb.append(next.text()+"\n");
					 next=next.nextElementSibling();
				}
				return sb.toString();
			}			
		}
		return "";
	}
	static String jsonStr="{\"title\":\"test\",\"isViewHead\":true,\"headRowSuffix\":0,"
			+ "\"widthArray\":[50, 110, 150,70,60,90,55,70,90,90,100,90],\"defTCWidth\":150,"
			+ "\"isAutoWidth\":false,\"isAutoremoveUpNullRows\":false,"
			+ "\"isAutoremoveDownNullRows\":true,\"isTrim\":false,"
			+ "\"readonlySuffix\":[0],attrSuffix:true}";
	private Text sqlparainput;
	@Override
	public void changeAfter(Object obj) {

	}

	@Override
	public void collect() {

	}

	@Override
	public String getMenuName() {
		return "UI测试平台";
	}

	@Override
	public String getMenuNameHead() {
		return "P000";
	}


	@Override
	public String getVersionFileJarFullPath() {
		return "/update.info";
	}

	@Override
	public void initCompositeMenu() {

	}

	@Override
	public void multiThreadOnRun() {

	}

	@Override
	public void multiThreadOnRunEnd() {

	}

	@Override
	public void disposeResources() {
		
	}	@Override
	public String precondition() {
		return null;
	}

	@Override
	public void startup() {
		
	}
}
