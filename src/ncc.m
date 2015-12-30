function [output_img] = ncc(image1, image2)
left_image = double(rgb2gray(image1));
right_image = double(rgb2gray(image2));
[Ml,Nl] = size(left_image);
DL_img = zeros(Ml,Nl);
sumr = 0;
for i = 1:Ml
    for j = 1:Nl
        sumr = sumr+left_image(i,j);
    end
end
aver = sumr/(Ml*Nl);

for i = 1:Ml
    for j = 1:Nl
        distance_min = 0;
        kd = 0;
        for d = 0:79
            init = 1;
            for fi = -2:2;
                for fj = -2:2
                    turni = i+fi;
                    turnj = j+fj;
                    if turni < 1
                        turni = 1;
                    end
                    if turnj < 1
                        turnj = 1;
                    end
                    if turni > Ml
                        turni = Ml;
                    end
                    if turnj > Nl
                        turnj = Nl;
                    end
                    FL(init) = left_image(turni,turnj);
                    turni = i+fi;
                    turnj = j-d+fj;
                    if turni < 1 || turnj < 1||turni > Ml||turnj > Nl
                        FR(init) = aver;
                    else
                        FR(init) = right_image(turni,turnj);
                    end
                    init = init+1;
                end
            end
            turn_distance = m_distance(FL,FR);
            if (turn_distance > distance_min)
                 distance_min = turn_distance;
                 kd = d;  
            end
        end
        DL_img(i,j) = kd;
    end
end
output_img =  DL_img;
end
function [distance] = m_distance(FL, FR)
length = size(FL');
a = double(FL);
b = double(FR);
distance = (a*b')/((sqrt(a*a'))*(sqrt(b*b')));
end