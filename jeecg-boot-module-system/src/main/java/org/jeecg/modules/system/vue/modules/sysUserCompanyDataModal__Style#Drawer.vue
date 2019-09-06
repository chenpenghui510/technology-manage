<template>
  <a-drawer
      :title="title"
      :width="800"
      placement="right"
      :closable="false"
      @close="close"
      :visible="visible"
  >

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="对应的用户ID">
          <a-input placeholder="请输入对应的用户ID" v-decorator="['userId', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="userName">
          <a-input placeholder="请输入userName" v-decorator="['userName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户性别(1:男 2:女)">
          <a-input-number v-decorator="[ 'userSex', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="身份证号">
          <a-input placeholder="请输入身份证号" v-decorator="['userCard', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="职务">
          <a-input placeholder="请输入职务" v-decorator="['userPost', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="职称">
          <a-input placeholder="请输入职称" v-decorator="['userTitle', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="固定电话">
          <a-input placeholder="请输入固定电话" v-decorator="['userTelephone', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="单位名称">
          <a-input placeholder="请输入单位名称" v-decorator="['companyName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="社会信用代码">
          <a-input placeholder="请输入社会信用代码" v-decorator="['socialCreditCode', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="主管部门">
          <a-input placeholder="请输入主管部门" v-decorator="['competentDepartment', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="单位性质">
          <a-input-number v-decorator="[ 'unitNature', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="工商注册地">
          <a-input placeholder="请输入工商注册地" v-decorator="['incorporationPlace', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司地址-省">
          <a-input placeholder="请输入公司地址-省" v-decorator="['companyAddressProvince', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司地址-市">
          <a-input placeholder="请输入公司地址-市" v-decorator="['companyAddressCity', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司地址-详细地址">
          <a-input placeholder="请输入公司地址-详细地址" v-decorator="['companyDetailAddress', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司电话">
          <a-input placeholder="请输入公司电话" v-decorator="['companyTelephone', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司传真">
          <a-input placeholder="请输入公司传真" v-decorator="['companyFax', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司邮箱">
          <a-input placeholder="请输入公司邮箱" v-decorator="['companyEmail', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司邮政编码">
          <a-input placeholder="请输入公司邮政编码" v-decorator="['companyPostalCode', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司银行账户">
          <a-input placeholder="请输入公司银行账户" v-decorator="['companyBankAccount', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司银行账号">
          <a-input placeholder="请输入公司银行账号" v-decorator="['companyBankCode', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="银行名称">
          <a-input placeholder="请输入银行名称" v-decorator="['bankName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="注册资本(万元)">
          <a-input placeholder="请输入注册资本(万元)" v-decorator="['registeredCapital', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="资本类型（1.人民币 2.美元）">
          <a-input-number v-decorator="[ 'capitalType', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="信用等级（1.AAA 2.AA 3.A）">
          <a-input-number v-decorator="[ 'creditRating', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="公司成立时间">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'companyCreateTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="经营范围">
          <a-input placeholder="请输入经营范围" v-decorator="['businessScope', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="法人">
          <a-input placeholder="请输入法人" v-decorator="['legalPerson', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="证件类型（1.身份证 2.护照 3.港澳台证）">
          <a-input-number v-decorator="[ 'documentType', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="证件号码">
          <a-input placeholder="请输入证件号码" v-decorator="['documentCode', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="法人手机号">
          <a-input placeholder="请输入法人手机号" v-decorator="['legalPhone', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="法人固定电话">
          <a-input placeholder="请输入法人固定电话" v-decorator="['legalTelephone', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="法人邮箱">
          <a-input placeholder="请输入法人邮箱" v-decorator="['legalEmail', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="学历(字典)">
          <a-input-number v-decorator="[ 'legalEducation', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="学位">
          <a-input-number v-decorator="[ 'legalAcademicDegree', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="法人职务">
          <a-input placeholder="请输入法人职务" v-decorator="['legalPost', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="法人性别(字典)">
          <a-input-number v-decorator="[ 'legalSex', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="法人出生日期">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'legalBirthDate', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="法人职称(字典)">
          <a-input-number v-decorator="[ 'legalTitle', {}]" />
        </a-form-item>
		
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "sysUserCompanyDataModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        },
        url: {
          add: "/system/sysUserCompanyData/add",
          edit: "/system/sysUserCompanyData/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'userId','userName','userSex','userCard','userPost','userTitle','userTelephone','companyName','socialCreditCode','competentDepartment','unitNature','incorporationPlace','companyAddressProvince','companyAddressCity','companyDetailAddress','companyTelephone','companyFax','companyEmail','companyPostalCode','companyBankAccount','companyBankCode','bankName','registeredCapital','capitalType','creditRating','businessScope','legalPerson','documentType','documentCode','legalPhone','legalTelephone','legalEmail','legalEducation','legalAcademicDegree','legalPost','legalSex','legalTitle'))
		  //时间格式化
          this.form.setFieldsValue({companyCreateTime:this.model.companyCreateTime?moment(this.model.companyCreateTime):null})
          this.form.setFieldsValue({legalBirthDate:this.model.legalBirthDate?moment(this.model.legalBirthDate):null})
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            formData.companyCreateTime = formData.companyCreateTime?formData.companyCreateTime.format('YYYY-MM-DD HH:mm:ss'):null;
            formData.legalBirthDate = formData.legalBirthDate?formData.legalBirthDate.format('YYYY-MM-DD HH:mm:ss'):null;
            
            console.log(formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })



          }
        })
      },
      handleCancel () {
        this.close()
      },


    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>