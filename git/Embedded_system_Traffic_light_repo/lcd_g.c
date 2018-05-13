#include <p32xxxx.h>

#pragma config FPLLMUL = MUL_20, FPLLIDIV = DIV_2, FPLLODIV = DIV_1, FWDTEN = OFF
#pragma config POSCMOD = HS, FNOSC = PRIPLL, FPBDIV = DIV_8

void initPortD(void);
void initPortB(void);
void initPortE(void);
void initPortF(void);
void delay(void);
void initLcd(void);
void writeXY(int x,int y,int lcd_cs);
void writeLcd(unsigned int num,int lcd_cs);
void tl1(void);
void tl2(void);
void tl3(void);
void tl4(void);
void timer(void);
void timer1(void);
void clear(void);
void busy(void);
void printMessage(char countDown);
void buzzer(void);

unsigned  int  portMap;

int main()
{
int i,j;
unsigned char x;
//clear();
	while(1)
{
	tl1();
	for(i=6 ; i>=0 ; i--)
	{
		x='0' + i;
		printMessage(x);
		timer();
	}
	buzzer();
	tl2();
	timer();
	//for (i=0 ; i<1000000 ; i++){}
	tl3();
	for(i=5 ; i>=0 ; i--)
	{
		x='0' + i;
		printMessage(x);
		timer();
	}
	buzzer();
	tl4();
	timer();
	//for (i=0 ; i<1000000 ; i++){}
	

}
}

void initPortD(void)
{
	unsigned int portMap;

	portMap = TRISD;
	portMap &= 0xFFFFFF4F;
	TRISD = portMap;
	PORTDbits.RD4 = 0;
	PORTDbits.RD5 = 0;
	PORTDbits.RD7 = 0;
}

void initPortB(void)
{
	unsigned int portMap;

	portMap = TRISB;
	portMap &= 0xFFFF7FFF;
	TRISB = portMap;
	PORTBbits.RB15 = 0;
}

void initPortE(void)
{
	unsigned int portMap;

	portMap = TRISE;
	portMap &= 0xFFFFFF00;
	TRISE = portMap;
	PORTE = 0x00;
}

void initPortF(void)
{
	unsigned int portMap;

	portMap = TRISF;
	portMap &= 0xFFFFFEF8;
	TRISF = portMap;
	PORTFbits.RF8 = 1;
}

void delay(void)
{
	unsigned int i;

	for(i=0;i<1600;i++);
}

void initLcd(void)
{	
	int CONTROL[4] = {0x40,0xB8,0xFF,0x3F};
	int i;

	PORTDbits.RD5 = 0;
	PORTBbits.RB15 = 0;
	PORTF = 0x01;
	PORTDbits.RD7 = 0;
	PORTDbits.RD7 = 1;
	PORTF = 0x02;
	PORTDbits.RD7 = 0;
	PORTDbits.RD7 = 1;
	PORTFbits.RF8 = 1;

	for(i = 0;i < 4;i++)
	{
		PORTE = CONTROL[i];
		PORTF = 0x01;
		PORTDbits.RD4 = 1;//enable=1
    	PORTDbits.RD4 = 0;//enable=0
		delay();
		PORTF = 0x02;
		PORTDbits.RD4 = 1;//enable=1
    	PORTDbits.RD4 = 0;//enable=0
		delay();	
	}
	PORTFbits.RF8 = 1;
}

void writeXY(int x,int y,int lcd_cs)
{
	PORTDbits.RD5 = 0;
	PORTBbits.RB15 = 0;
	PORTF = lcd_cs;
	PORTE = 0x40 + x;
	PORTDbits.RD4 = 1;//enable=1
    PORTDbits.RD4 = 0;//enable=0
	delay();
	PORTE = 0xB8 + y;
	PORTDbits.RD4 = 1;//enable=1
    PORTDbits.RD4 = 0;//enable=0
	delay();
	PORTFbits.RF8 = 1;
}

void writeLcd(unsigned int num,int lcd_cs)
{
	int i;

	PORTDbits.RD5 = 0;
	PORTBbits.RB15 = 1;
	PORTF = lcd_cs;
	PORTE = num;
    PORTDbits.RD4 = 1;//enable=1
    PORTDbits.RD4 = 0;//enable=0
	delay();
	PORTFbits.RF8 = 1;
}
void tl1(void)
{
int i,j;
unsigned int DATA1[23] = {
								0xff,//0b11111111,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xf9,//0b11111001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b11111001,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xff,//0b11111111,
							};
unsigned int DATA2[23] = {
								0xff,//0b11111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0x3f,//0b00111111,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0xff,//0b11111111
							};	
unsigned int DATA3[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA4[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0x3f,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x3f,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA5[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA6[23] = {
								0xff,
								0x80 ,
								0x80 ,
								0x9f,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x9f,
								0x80 ,
								0x80 ,
								0xff,
							};					
	initPortD();
	initPortB();
	initPortE();
	initPortF();
	initLcd();
	
	writeXY(40,1,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA1[i],1);
	writeXY(40,2,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA2[i],1);
	writeXY(40,3,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA3[i],1);
	writeXY(40,4,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA4[i],1);
	writeXY(40,5,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA5[i],1);
	writeXY(40,6,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA6[i],1);
}
void tl2(void)
{
int i,j;
unsigned int DATA1[23] = {
								0xff,//0b11111111,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xf9,//0b11111001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b00001001,
								0xf9,//0b11111001,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xff,//0b11111111,
							};
unsigned int DATA2[23] = {
								0xff,//0b11111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0x3f,//0b00111111,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00100000,
								0x3f,//0b00111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0xff,//0b11111111
							};	
unsigned int DATA3[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA4[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA5[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA6[23] = {
								0xff,
								0x80 ,
								0x80 ,
								0x9f,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x9f,
								0x80 ,
								0x80 ,
								0xff,
							};					
	initPortD();
	initPortB();
	initPortE();
	initPortF();
	initLcd();
	
	writeXY(40,1,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA1[i],1);
	writeXY(40,2,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA2[i],1);
	writeXY(40,3,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA3[i],1);
	writeXY(40,4,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA4[i],1);
	writeXY(40,5,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA5[i],1);
	writeXY(40,6,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA6[i],1);
}
void tl3(void)
{
int i,j;
unsigned int DATA1[23] = {
								0xff,//0b11111111,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xf9,//0b11111001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0xf9,//0b11111001,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xff,//0b11111111,
							};

	unsigned int DATA2[23] = {
								0xff,//0b11111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0x3f,//0b00111111,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x3f,//0b00111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0xff,//0b11111111
							};	
unsigned int DATA3[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA4[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0x3f,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x3f,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA5[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA6[23] = {
								0xff,
								0x80 ,
								0x80 ,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x9f,
								0x80 ,
								0x80 ,
								0xff,
							};	
			
	initPortD();
	initPortB();
	initPortE();
	initPortF();
	initLcd();

	
	writeXY(40,1,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA1[i],1);
	writeXY(40,2,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA2[i],1);
	writeXY(40,3,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA3[i],1);
	writeXY(40,4,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA4[i],1);
	writeXY(40,5,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA5[i],1);
	writeXY(40,6,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA6[i],1);
}
void tl4(void)
{
unsigned int i,j;
	unsigned int DATA1[23] = {
								0xff,//0b11111111,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xf9,//0b11111001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0xf9,//0b11111001,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xff,//0b11111111,
							};

	unsigned int DATA2[23] = {
								0xff,//0b11111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0x3f,//0b00111111,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x3f,//0b00111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0xff,//0b11111111
							};	
unsigned int DATA3[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA4[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x3f,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA5[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA6[23] = {
								0xff,
								0x80 ,
								0x80 ,
								0x9f,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x9f,
								0x80 ,
								0x80 ,
								0xff,
							};	
			
	initPortD();
	initPortB();
	initPortE();
	initPortF();
	initLcd();

	
	writeXY(40,1,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA1[i],1);
	writeXY(40,2,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA2[i],1);
	writeXY(40,3,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA3[i],1);
	writeXY(40,4,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA4[i],1);
	writeXY(40,5,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA5[i],1);
	writeXY(40,6,1);
	for(i = 0;i < 23;i++)
		writeLcd(DATA6[i],1);
}

void timer(void)
         {
T2CONbits.ON=0;
T2CONbits.TGATE=0;
T2CONbits.TCS=0;
T2CONbits.T32=1;//mode 32bit
T2CONbits.TCKPS0=1;//????? 256
T2CONbits.TCKPS1=1;
T2CONbits.TCKPS2=1;
T3CONbits.ON=0;
TMR3=0;//TMRy
TMR2=0;//TMRx
PR2=0X9896;//PR2x
PR3=0x0;//PR2y
T2CONbits.ON=1;//start timer
IFS0bits.T2IF=0;
IFS0bits.T3IF=0;//
while(!IFS0bits.T3IF){//timer stop
			//int countDown = 5-(TMR3+TMR2);
			//printMessage('d');
			}			
      }

void timer1(void)
      {
	T1CONbits.ON=0;
	T1CONbits.TGATE=0;
	T1CONbits.TCS=0;//in clock
	T1CONbits.TCKPS0=1;
	T1CONbits.TCKPS1=1;
	//T1CONbits.TCKPS2=1;
	T1CONbits.TSYNC=1;
	TMR1=0;
	PR1=0X04FF;
	T1CONbits.ON=1;
	IFS0bits.T1IF=0;
	while(!IFS0bits.T1IF);
      }

/*
	unsigned int DATA1[23] = {
								0xff,//0b11111111,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xf9,//0b11111001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0x09,//0b00001001,
								0xf9,//0b11111001,
								0x1 ,//0b00000001,
								0x1 ,//0b00000001,
								0xff,//0b11111111,
							};

	unsigned int DATA2[23] = {
								0xff,//0b11111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0x3f,//0b00111111,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x20,//0b00100000,
								0x3f,//0b00111111,
								0x0 ,//0b00000000,
								0x0 ,//0b00000000,
								0xff,//0b11111111
							};	
unsigned int DATA3[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA4[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0x3f,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x20,
								0x3f,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA5[23] = {
								0xff,
								0x0 ,
								0x0 ,
								0xfc,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0x4,
								0xfc,
								0x0 ,
								0x0 ,
								0xff,
							};
unsigned int DATA6[23] = {
								0xff,
								0x80 ,
								0x80 ,
								0x9f,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x90,
								0x9f,
								0x80 ,
								0x80 ,
								0xff,
							};	
*/				

void clear(void)
{
int i,j;
initPortD();
	initPortB();
	initPortE();
	initPortF();
	initLcd();
	for(i = 0;i < 8;i++)
	{
		for(j = 0;j < 64;j++)
		{
			writeXY(j,i,1);
			writeLcd(0x00,1);
			writeXY(j,i,2);
			writeLcd(0x00,2);
		}
	}
}

void printMessage(char countDown)
{
char control[7]={0x38,0x38,0x38,0xe,0x6,0x1,0x40};
// set CGRAM=0x40
	int i;
	portMap = TRISB;
	portMap &= 0xFFFF7FFF;
	TRISB = portMap;
AD1PCFG |=0x8000; // RB15-Digital
portMap = TRISF;
	portMap &= 0xFFFFFEF8;
	TRISF = portMap;
	PORTFbits.RF8 = 1;
       
       portMap = TRISE;
	portMap &= 0xFFFFFF00;
	TRISE = portMap;
       
       portMap = TRISD;
	portMap &= 0xFFFFFFCF;
	TRISD = portMap;
	PORTF = 0x00;
 	PORTDbits.RD5 = 0;//w/r
 	PORTBbits.RB15 = 0;//rs 
	for(i = 0;i < 7;i++)
     {
        PORTE=control[i];
        PORTDbits.RD4=1;//enable=1
        PORTDbits.RD4=0;//enable=0
          busy();
      }
  PORTE=0x80;//DDRAM
  PORTDbits.RD4=1;//enable=1
  PORTDbits.RD4=0;//enable=0
            busy();
  PORTBbits.RB15 = 1;//rs 

		PORTE=countDown;
        PORTDbits.RD4=1;//enable=1
        PORTDbits.RD4=0;//enable=0
        busy();
}
void busy(void)
{
        char RD,RS;
        int STATUS_TRISE;
    RD=PORTDbits.RD5;
    RS=PORTBbits.RB15;
    STATUS_TRISE=TRISE;
	PORTDbits.RD5 = 1;//w/r
	PORTBbits.RB15 = 0;//rs 
    portMap = TRISE;
	portMap |= 0x80;
	TRISE = portMap;
do
     {
     PORTDbits.RD4=1;//enable=1
     PORTDbits.RD4=0;//enable=0
     }
   while(PORTEbits.RE7); // BF ????? ???
        PORTDbits.RD5=RD; 
        PORTBbits.RB15=RS;
        TRISE=STATUS_TRISE;   
}
void buzzer(void)
{
	int i;
	PORTGbits.RG15 = 0;//buzzer=0ff
	TRISG &= 0xFFFF7FFF;
	PORTGbits.RG15 = 1;
	for(i=0 ; i<100000 ; i++){}
	PORTGbits.RG15 = 0;
}




