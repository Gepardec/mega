def recreateServiceResources(String service, String stage = "") {
    if (stage != "") {
        stage = "." + stage
    }
    def processedYaml = "${service}.processed.yaml"
    sh "oc process --filename ${service}.yaml \
                   --param-file=${service}${stage}.properties \
                   --output yaml \
                   > ${processedYaml} \
        && oc delete --ignore-not-found \
                     --filename ${processedYaml} \
        && oc create --filename ${processedYaml}"
}

return this