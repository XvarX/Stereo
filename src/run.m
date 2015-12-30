 function y=run()
 Files = dir(fullfile('C:\Users\Xvar\Desktop\ALL-2views',''));
 for i = 3:23
     view1path = fullfile(Files(i).name,'view1.png');
     view5path = fullfile(Files(i).name,'view5.png');
     image1 = imread(view1path);
     image2 = imread(view5path);
     output1 = map(image1,image2);
     file1 = strcat(num2str(2*i-1),'.png');
     imwrite(uint8(output1)*3,file1);
     output2 = map2(image2,image1);
     file2 = strcat(num2str(2*i),'.png');
     imwrite(uint8(output2)*3,file2);
 end