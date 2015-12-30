function [distance] = m_distance(FL, FR)
length = size(FL');
distance = 0;
for disti = 1:length
    distance = distance+(double(FL(disti)) - double(FR(disti)))^2;
end
end