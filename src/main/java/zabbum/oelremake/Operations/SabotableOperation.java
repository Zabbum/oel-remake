package zabbum.oelremake.Operations;

import zabbum.oelremake.AbstractIndustry;

public interface SabotableOperation {
    /**
     * Make it possible to get the list of industries in other situations than those predicted in Operations
     *
     * @return
     */
    public AbstractIndustry[] getIndustries();
}
