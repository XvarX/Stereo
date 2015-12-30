function [output_img] = map2(image1, image2)
left_image = rgb2gray(image1);
right_image = rgb2gray(image2);
[Ml,Nl] = size(left_image);
DL_img = zeros(Ml,Nl);
for i = 1:Ml
    for j = 1:Nl
        distance_min = 1111111111111111111111111111111111111111;
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
                    turnj = j+d+fj;
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
                    FR(init) = right_image(turni,turnj);
                    init = init+1;
                end
            end
            turn_distance = m_distance(FL,FR);
            if (turn_distance < distance_min)
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
distance = 0;
for disti = 1:length
    distance = distance+(double(FL(disti)) - double(FR(disti)))^2;
end
end