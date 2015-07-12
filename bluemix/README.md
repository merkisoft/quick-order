
## deployment with cf

download cf from ibm bluemix

cf set-env "Quick+Order" JBP_CONFIG_IBMJDK "version: 1.8.+"
restage "Quick Order"


cf push "Quick Order" -p server_war.war 
cf logs "Quick Order" --recent


## ant hacks

add the following to the generated ant task artifact.server:war

    <copy todir="${artifact.output.server:war_exploded}">
      <fileset dir="${basedir}/web/dist"/>
    </copy>

before:

    <zip destfile="${temp.jar.path.server_war.war}">
      <zipfileset dir="${artifact.output.server:war_exploded}"/>

