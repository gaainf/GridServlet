#!/bin/bash

servlet=SeleniumGridServlet
build_dir=build

java -cp "${build_dir}/*" org.openqa.grid.selenium.GridLauncherV3 -role hub -port 4444 -servlets ${servlet}
