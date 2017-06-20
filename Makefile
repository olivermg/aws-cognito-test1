
attach-policy:
	aws iot attach-principal-policy --policy-name iotpolicy1 --principal eu-west-1:xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx

list-principals:
	aws iot list-policy-principals --policy-name iotpolicy1

run:
	./run.sh

