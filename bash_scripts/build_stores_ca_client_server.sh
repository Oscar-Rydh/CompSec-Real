
printf " \n\n\n\n\n\n   LETS BUILD!! \n   This script builds a self-signed CA and a clientside/serverside \n"

function pause(){
 read -n1 -rsp $'   Press any key to continue or Ctrl+C to exit...\n\n\n'
}

pause
    
bash build_client_side.sh
bash build_server_side.sh
    	







