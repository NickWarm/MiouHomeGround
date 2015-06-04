/*
範例檔名:opencv(視訊運動範圍偵測)
程式開發:陳映綱
*/

#include <opencv/cv.h>
#include <opencv/highgui.h> 
#include <opencv/cxcore.h>
#include <iostream>

using namespace std;

int t = 66;//每張影像間隔時間(毫秒)
int framenumber = 0;//影像編號
int threshold = 16;//二值化閥值
int take = 0;//擷圖迴圈數
int luma_gate = 20;
int con = 4;

int main()
{
	CvCapture * cap = cvCreateCameraCapture( 0 );
	IplImage * frame = cvQueryFrame( cap );
	CvSize size = cvSize( frame->width, frame->height );
	IplImage * frame_gray = cvCreateImage( size, 8, 1 );
	cvZero( frame_gray );
	IplImage * exframe = cvCreateImage( size, 8, 1 );
	cvZero( exframe );
	IplImage * diff = cvCreateImage( size, 8, 1 );
	cvZero( diff );
	IplImage * diff_threshold = cvCreateImage( size, 8, 1 );
	cvZero( diff_threshold );
	cvNamedWindow( "Video", 1 );

	jump1:
	while( 1 )
	{
		frame = cvQueryFrame( cap );
		frame_gray = cvCreateImage( size, 8, 1 );
		cvCvtColor( frame, frame_gray, CV_RGB2GRAY );
		
		if( framenumber == 0 || framenumber % 2 == 0 )
		{
			cvCopyImage( frame_gray, exframe );
			cvShowImage( "Video", frame );
			cvWaitKey( t );
			framenumber++;
			goto jump1;
		}

		if( framenumber > 0 && framenumber % 2 == 1 )
		{
			cvAbsDiff( frame_gray, exframe, diff );
			cvThreshold( diff, diff_threshold, threshold, 255, CV_THRESH_BINARY );

			CvScalar s; int sum = 0, avg = 0, area = ( diff->height * diff->width );
			for( int i = 0; i < diff_threshold->height; i++ )
			{
				for( int j = 0; j < diff_threshold->width; j++ )
				{
					s = cvGet2D( diff_threshold, i, j );
					sum = sum + s.val[0];
				}
			}
			avg = sum / area;
			cout << "f" << framenumber << " - " << "f" << framenumber - 1 << " = " << avg << endl;
			framenumber++;

			if( avg > luma_gate )
			{
				take++;

				if( take % con == 0 )
				{
					if( take == con ) cvSaveImage( "D:\\Move_01.jpg", frame );
					if( take == con * 2 ) cvSaveImage( "D:\\Move_02.jpg", frame );
					if( take == con * 3 ) cvSaveImage( "D:\\Move_03.jpg", frame );
					if( take == con * 4 ) cvSaveImage( "D:\\Move_04.jpg", frame );
					if( take == con * 5 ) cvSaveImage( "D:\\Move_05.jpg", frame );
				}

				if( take > con * 5 + 1 )
				{
					cvWaitKey( t );
					cvDestroyWindow( "Video" );
					cvReleaseCapture( &cap );
					cvReleaseImage( &frame_gray );
					break;
				}

			}

			cvShowImage( "Video", frame );
			cvShowImage( "Luma_Diff", diff_threshold );
			cvWaitKey( t );

		}

	}
	
}

